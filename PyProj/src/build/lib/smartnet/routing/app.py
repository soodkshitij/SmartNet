from flask import Flask, request,abort,jsonify,current_app,make_response
from math import radians, sin, cos, atan2, sqrt
import json
import datetime
from smartnet.impl.DataAccessor import InsertUser, VerifyEmailPassword, getUserByEmail,\
InsertForgotPassword,GetForgotPassword,ResetUserPassword,GetUsersByCommunityId,\
InsertIntraPost, InsertInterPost, getUserById, GetIntraPostsByCommunityId, DeleteComment, InsertComment,\
GetCommentsByPostId,InsertMessage,GetUsersByToUserId,GetMessages,GetCommunitiesByNotId,get_members_count,GetCommunitiesById,\
GetInterPostsByUserId, GetIntraPostsByUserId,get_file_obj,\
    GetInterPostsByCommunityId,GetCommunity,assign_c_admin,activate_c, get_pending_users,markUserAsActive,get_p_ip,activate_ip,GetCommunities,GetCommunityAndCoordinates
from smartnet.utils.utils import decode_auth_token,encode_auth_token
from smartnet.utils.DObj import DObject
from smartnet.service import s3service
import uuid
import os
from datetime import timedelta
from functools import update_wrapper

app = Flask(__name__)

def crossdomain(origin=None, methods=None, headers=None,
                max_age=21600, attach_to_all=True,
                automatic_options=True):
    if methods is not None:
        methods = ', '.join(sorted(x.upper() for x in methods))
    if headers is not None and not isinstance(headers, basestring):
        headers = ', '.join(x.upper() for x in headers)
    if not isinstance(origin, basestring):
        origin = ', '.join(origin)
    if isinstance(max_age, timedelta):
        max_age = max_age.total_seconds()

    def get_methods():
        if methods is not None:
            return methods

        options_resp = current_app.make_default_options_response()
        return options_resp.headers['allow']

    def decorator(f):
        def wrapped_function(*args, **kwargs):
            if automatic_options and request.method == 'OPTIONS':
                resp = current_app.make_default_options_response()
            else:
                resp = make_response(f(*args, **kwargs))
            if not attach_to_all and request.method != 'OPTIONS':
                return resp

            h = resp.headers

            h['Access-Control-Allow-Origin'] = origin
            h['Access-Control-Allow-Methods'] = get_methods()
            h['Access-Control-Max-Age'] = str(max_age)
            if headers is not None:
                h['Access-Control-Allow-Headers'] = headers
            return resp

        f.provide_automatic_options = False
        return update_wrapper(wrapped_function, f)
    return decorator

@app.route("/login", methods=["POST"])
def Login():
    content = request.json
    print(content['email'])
    print(content['password'])
    user = VerifyEmailPassword(content['email'],content['password'])

    if user is None:
        return json.dumps({"token":None,"verified":False,"message":"User does not exist","community_id":None})
    if user is not None and user.verified:
        token =  encode_auth_token(user.id)
        return json.dumps({"token":token,"verified":True,"message":"User exists and verified","community_id":user.community_id})
    else:
        return json.dumps({"token":None,"verified":False,"message":"Profile is pending with admin for approval","community_id":None})


@app.route("/decode",methods=["POST"])
def Decode():
    content = request.json
    user_id = decode_auth_token(content['token'])
    duser = getUserById(user_id)
    user = DObject()
    user.id = duser.id
    user.first_name = duser.first_name
    user.last_name = duser.last_name
    user.community_id = duser.community_id
    user.image = "https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+duser.first_name[0].lower()+".png"
    return json.dumps({"userId":decode_auth_token(content['token']),'user':user.__dict__})

@app.route("/signup",methods=["POST"])
def SignUp():
    content = request.json
    print content
    exist = getUserByEmail(content['email'])
    if exist is not None:
        return json.dumps({"success":False,"message":"User already exist with this email"})
    
    if InsertUser(content['firstName'],content['lastName'],content['email'],content['password'],content['phoneNumber'],content['community_id']):
        return json.dumps({"success":True,"message":"Valid user"})
    else:
        return json.dumps({"success":False,"message":"OOPS!Something went wrong :-(."})

@app.route("/inter-post",methods=["POST"])
def PostInterPost():
    content = request.json
    for c_id in content["communityIds"].split(','):
        InterPostObj = DObject()
        InterPostObj.title = content["title"]
        InterPostObj.content = content["content"]
        InterPostObj.category = content['category']
        InterPostObj.timestamp = datetime.datetime.now()
        InterPostObj.image_ids = content['image_ids']
        InterPostObj.user_id = decode_auth_token(content['token'])
        InterPostObj.community_id = c_id
        #InterPostObj.post_type = content["postType"]
        InterPostObj.admin_approved = False
        InterPostObj.image_ids = content['image_ids']
        InsertInterPost(InterPostObj)
    return json.dumps({"success":True})

@app.route("/intra-post",methods=["GET"])
def getInterPost():
    community_id = request.args.get('community_id')
    offset = request.args.get('offset')
    limit = request.args.get('limit')
    filter_category = request.args.get('category')
    if filter_category:
        posts = GetIntraPostsByCommunityId(community_id, limit, offset, filter_category)
    else:
        posts = GetIntraPostsByCommunityId(community_id, limit, offset)
    post_obj = []
    for p in posts:
        print p
        obj = DObject()
        obj.user_id = p[0].id
        obj.post_id = p[1].id
        obj.first_name = p[0].first_name
        obj.last_name = p[0].last_name
        obj.community_name = p[2].name
        obj.title = p[1].title
        obj.message = p[1].content
        obj.category = p[1].category
        obj.timestamp = str(p[1].timestamp)
        if p[1].file_id:
            k =get_file_obj(p[1].file_id).key
            if k is None:
                obj.file_id = None
            else:
                file_id = (k).split(",")[0]
                obj.file_id = "https://s3.amazonaws.com/s3-cmpe-281/"+file_id
        else:
            obj.file_id = None
        
        obj.image = "https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+ p[0].first_name[0].lower()+".png"
        post_obj.append(obj.__dict__)
    
    if filter_category:
        posts = GetInterPostsByCommunityId(community_id, filter_category, limit, offset)
    else:
        posts = GetInterPostsByCommunityId(community_id, None,limit, offset)
    for p in posts:
        print p
        obj = DObject()
        obj.user_id = p[0].id
        obj.post_id = p[1].id
        obj.first_name = p[0].first_name
        obj.last_name = p[0].last_name
        obj.community_name = (GetCommunity(p[0].community_id)).name
        obj.title = p[1].title
        obj.message = p[1].content
        obj.category = p[1].category
        obj.timestamp = str(p[1].timestamp)
        if p[1].file_id:
            k =get_file_obj(p[1].file_id).key
            if k is None:
                obj.file_id = None
            else:
                file_id = (k).split(",")[0]
                obj.file_id = "https://s3.amazonaws.com/s3-cmpe-281/"+file_id
        else:
            obj.file_id = None
        
        obj.image = "https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+ p[0].first_name[0].lower()+".png"
        post_obj.append(obj.__dict__)
    
    return json.dumps(post_obj)

@app.route("/intra-post",methods=["POST"])
def PostIntraPost():
    content = request.json

    IntraPostObj = DObject()
    IntraPostObj.title = content["title"]
    IntraPostObj.category = content['category']
    IntraPostObj.content = content["content"]
    IntraPostObj.timestamp = datetime.datetime.now()
    IntraPostObj.user_id = decode_auth_token(content['token'])
    IntraPostObj.community_id = content["communityId"]
    IntraPostObj.image_ids = content['image_ids']
    #IntraPostObj.post_type = content["postType"]

    InsertIntraPost(IntraPostObj)
    return json.dumps({"success":True})

@app.route("/forgot-password/<p_uuid>",methods=["GET"])
@app.route("/forgot-password",methods=["POST"],defaults={'p_uuid':None})
def ForgotPassword(p_uuid=None):
    if request.method == 'POST':
        content = request.json
        user = getUserByEmail(content['email'])
        if user is not None:
            ForgotPasswordObj = DObject()
            ForgotPasswordObj.uuid = uuid.uuid4()
            ForgotPasswordObj.timestamp = datetime.datetime.now() 
            ForgotPasswordObj.user_id = user.id
            ForgotPasswordObj.used = False
            if not InsertForgotPassword(ForgotPasswordObj):
                return json.dumps({"success":False,"uuid":None})
            else:
                return json.dumps({"success":True,"uuid":str(ForgotPasswordObj.uuid)})
        else:
            return json.dumps({"success":False,"message":"No such user"})
    else:
        forgotPassword = GetForgotPassword(p_uuid)

        if not forgotPassword:
            return json.dumps({"exist":False,"expired":False})
        else:
            time_today = datetime.datetime.now() 
            delta = time_today - forgotPassword.timestamp
            delta_hours = delta.days * 24 + delta.seconds / 3600.0

            if delta_hours>=1:
                return json.dumps({"exist":True,"expired":True})
            else:
                return json.dumps({"exist":True,"expired":False})

@app.route("/reset-password",methods=["POST"])
def ResetPassword():
    content = request.json

    forgotPassword = GetForgotPassword(content['p_uuid'])

    if ResetUserPassword(forgotPassword.user_id,content['password']):
        return json.dumps({"message":"Password Reset Successfully","success":True})
    else:
        return json.dumps({"message":"Password Reset Not Successfull","success":False})


@app.route("/users",methods=["GET"])
def GetUsers():
    c_id = request.args.get('community_id')
    users = GetUsersByCommunityId(int(c_id))

    if len(users) == 0:
        return json.dumps({"message":"No Users in this community","success":False})
    else:
        ret_users = []

        for duser in users:
            user = DObject()
            user.id = duser.id
            user.first_name = duser.first_name
            user.last_name = duser.last_name
            user.image = "https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+duser.first_name[0].lower()+".png"
            ret_users.append(user.__dict__)
        return json.dumps(ret_users)

@app.route("/delete-comment",methods=["POST"])
def deleteComment():
    content = request.json
    if DeleteComment(content['id'],decode_auth_token(content['token'])):
        return json.dumps({"success":True})
    else:
        return json.dumps({"success":False})

@app.route("/comment",methods=["POST"])
def PostComment():
    content = request.json
    print content
    CommentObj = DObject()
    CommentObj.content = content["content"]
    CommentObj.user_id = decode_auth_token(content['token'])
    CommentObj.post_id = content["postId"]
    
    res, id = InsertComment(CommentObj)
    print res
    if res:
        return json.dumps({"success":True,"id":id})
    else:
        return json.dumps({"success":False,"id":0})

@app.route("/getCommentsByPostId",methods=["GET"])
def GetComment():
    post_id = request.args.get('post_id')
    data = GetCommentsByPostId(post_id)
    if not data:
        return json.dumps([])
    else:
        ret_comments = []

        for d in data:
            comment = d[0]
            user = d[1]
            dcomment = DObject()
            dcomment.id = comment.id
            dcomment.content = comment.content
            dcomment.user_id = comment.user_id
            dcomment.post_id = comment.post_id
            dcomment.first_name = user.first_name
            dcomment.last_name = user.last_name
            ret_comments.append(dcomment.__dict__)
        return json.dumps(ret_comments)

@app.route("/postMessage",methods=["POST"])
def PostMessage():
    content = request.json
    MessageObj = DObject()
    MessageObj.title = ""
    MessageObj.content = content["content"]
    #MessageObj.timestamp = content["timestamp"]
    MessageObj.from_user_id = content["from_user_id"]
    MessageObj.to_user_id = content["to_user_id"]

    if InsertMessage(MessageObj):
        return json.dumps({"success":True})
    else:
        return json.dumps({"success":False})
    
@app.route("/message-users-list",methods=["GET"])
def GetMessageUsers():
    to_user_id = request.args.get('to_user_id')
    data = GetUsersByToUserId(to_user_id)
    ret_messages = []

    if not data:
        return json.dumps([])
    x= []
    for d in data:
        user = d[1]
        if user.id in x:
            continue
        x.append(user.id)
        complex_obj = DObject()
        complex_obj.id = user.id
        complex_obj.first_name = user.first_name
        complex_obj.last_name = user.last_name
        complex_obj.image ="https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+user.first_name[0].lower()+".png"
    
        ret_messages.append(complex_obj.__dict__)

    return json.dumps(ret_messages)


@app.route("/messages",methods=["GET"])
def getMessagesFromUser():
    to_user_id = request.args.get('to_user_id')
    from_user_id = request.args.get('from_user_id')
    
    data = GetMessages(from_user_id,to_user_id)
    ret_messages = []

    if not data:
        return json.dumps([])
    
    for d in data:
        message = d
        
        
        complex_obj = DObject()
        complex_obj.title = message.title
        complex_obj.content = message.content
        complex_obj.timestamp = str(message.timestamp)
        complex_obj.from_user_id = message.from_user_id
        complex_obj.to_user_id = message.to_user_id

        ret_messages.append(complex_obj.__dict__)

    return json.dumps(ret_messages)

@app.route("/not-communities/",methods=["GET"])
def NotCommunity():
    community_id = request.args.get('community_id')
    data = GetCommunitiesByNotId(community_id)
    ret_messages = []

    if not data:
        return json.dumps([])
    
    for d in data:
        c = d[0]
        g = d[1]
        
        complex_obj = DObject()
        complex_obj.id = c.id
        complex_obj.name = c.name
        complex_obj.lat = g.lat
        complex_obj.lon = g.lon

        ret_messages.append(complex_obj.__dict__)

    return json.dumps(ret_messages)

@app.route("/getMembersCount",methods=["GET"])
def getMembersCount():
    community_id = request.args.get('community_id')
    count = get_members_count(community_id)
    return json.dumps({'count':count})

@app.route("/getCommunityById",methods=["GET"])
def getCommunityById():
    community_id = request.args.get('community_id')
    data = GetCommunitiesById(community_id)
    ret_messages=[]
    if not data:
        return json.dumps([])
    
    for d in data:
        c = d[0]
        g = d[1]
        
        complex_obj = DObject()
        complex_obj.id = c.id
        complex_obj.name = c.name
        complex_obj.lat = g.lat
        complex_obj.lon = g.lon

        ret_messages.append(complex_obj.__dict__)

    return json.dumps(ret_messages)

@app.route("/interpost-user-id",methods=["GET"])
def GetInterPostUserId():
    user_id = request.args.get('user_id')
    data = GetInterPostsByUserId(user_id)
    user = getUserById(user_id)
    ret_data = []
    for p in data:
        obj = DObject()
        obj.user_id = user.id
        obj.post_id = p[0].id
        obj.first_name = user.first_name
        obj.last_name = user.last_name
        obj.community_name = p[1].name
        obj.title = p[0].title
        obj.message = p[0].content
        obj.category = p[0].category
        obj.timestamp = str(p[0].timestamp)
        obj.admin_approved = p[0].admin_approved
        if p[0].file_id:
            k =get_file_obj(p[0].file_id).key
            if k is None:
                obj.file_id = None
            else:
                file_id = (k).split(",")[0]
                obj.file_id = "https://s3.amazonaws.com/s3-cmpe-281/"+file_id
        else:
            obj.file_id = None
        
        obj.image = "https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+ user.first_name[0].lower()+".png"
        ret_data.append(obj.__dict__)
    return json.dumps(ret_data)

@app.route("/intrapost-user-id",methods=["GET"])
def GetIntraPostUserId():
    user_id = request.args.get('user_id')
    data = GetIntraPostsByUserId(user_id)
    ret_data = []
    if data is None:
        return json.dumps([])
    user = getUserById(user_id)
    for p in data:
        print p
        obj = DObject()
        obj.user_id = user.id
        obj.post_id = p[0].id
        obj.first_name = user.first_name
        obj.last_name = user.last_name
        obj.community_name = p[1].name
        obj.title = p[0].title
        obj.message = p[0].content
        obj.category = p[0].category
        obj.timestamp = str(p[0].timestamp)
        if p[0].file_id:
            k =get_file_obj(p[0].file_id).key
            if k is None:
                obj.file_id = None
            else:
                file_id = (k).split(",")[0]
                obj.file_id = "https://s3.amazonaws.com/s3-cmpe-281/"+file_id
        else:
            obj.file_id = None
        
        obj.image = "https://d19rpgkrjeba2z.cloudfront.net/e82de29ec673c6fa/static/nextdoorv2/images/avatars/avatar-"+ user.first_name[0].lower()+".png"
        ret_data.append(obj.__dict__)
    return json.dumps(ret_data)
    
    
    
@app.route('/upload-file',methods=['POST','OPTIONS'])
@crossdomain(origin='*')
def post_file():
    print("inside post")
    name = uuid.uuid4().hex
    file = request.files['upl']
    file.save(os.path.join("/tmp/", name))
    res = s3service.upload(os.path.join("/tmp/", name))
    s = os.path.getsize(os.path.join("/tmp/", name))
    return json.dumps({'files':[{'public_id':res,'size':s,'name':file.filename}]})

@app.route('/assign-c-admin',methods=['GET'])
def assign_community_admin():
    c_email = request.args.get("community_admin_email")
    c_id = request.args.get("community_id")
    msg = assign_c_admin(c_email,c_id)
    return json.dumps({'msg':msg})

@app.route('/activate-community',methods=['GET'])
def activate_community():
    c_id = request.args.get("community_id")
    msg = activate_c(c_id)
    return json.dumps({'msg':msg})

@app.route('/pending-users',methods=['GET'])
def pd():
    admin_id = request.args.get("admin_id")
    ret_data = []
    data = get_pending_users(int(admin_id))
    for d in data:
        complex_obj = DObject()
        complex_obj.id = d.id
        complex_obj.f_name = d.first_name
        complex_obj.l_name = d.last_name
        complex_obj.email =d.email
        complex_obj.phone = d.phone_number

        ret_data.append(complex_obj.__dict__)
    return json.dumps(ret_data)  

@app.route("/activate-user",methods=['GET'])
def zd():
    user_id = request.args.get("user_id")
    msg = markUserAsActive(int(user_id))
    return json.dumps({'msg':msg})

@app.route("/get-pending-inter-post",methods=['GET'])
def io():
    user_id = request.args.get("user_id")
    ret_data = []
    data = get_p_ip(int(user_id))
    for d in data:
        complex_obj = DObject()
        complex_obj.id = d.id
        complex_obj.title = d.title
        complex_obj.content = d.content
        ret_data.append(complex_obj.__dict__)
    return json.dumps(ret_data)   

@app.route("/activate-ip",methods=['GET'])
def iop():
    post_id = request.args.get("id")
    msg = activate_ip(post_id)
    return json.dumps({'msg':msg})

@app.route('/get-community-id',methods=['GET'])
def get_comm_id():
    lat = request.args.get('lat')
    lon = request.args.get('lon')
    print "ya"
    data = GetCommunityAndCoordinates()

    if data is None:
        return json.dumps({"success":False,"message":"No Communities"})

    min_id = -1

    min_dist = 10000000.0
    min_name = ""
    min_address = ""

    for d in data:
        if GetDistance(lat,lon,d[1].lat,d[1].lon) < min_dist:
            min_dist = GetDistance(lat,lon,d[1].lat,d[1].lon)
            min_id = d[0].id
            min_name = d[0].name
            min_address = d[0].street_address
            

    return json.dumps({"success:":"true","communityId":min_id,"communityAddress":min_address,"communityName":min_name})

def GetDistance(plat1,plon1,plat2,plon2):

    R = 6373.0
    lat1 = radians(float(plat1))
    lon1 = radians(float(plon1))
    lat2 = radians(float(plat2))
    lon2 = radians(float(plon2))

    dlon = lon2 - lon1
    dlat = lat2 - lat1

    a = sin(dlat / 2)**2 + cos(lat1) * cos(lat2) * sin(dlon / 2)**2
    c = 2 * atan2(sqrt(a), sqrt(1 - a))

    distance = R * c
    return distance

    
    
    
        
if __name__ == '__main__':
    app.run(debug=True,host="0.0.0.0")