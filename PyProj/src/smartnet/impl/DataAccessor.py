from elixir import *
from sqlalchemy.sql.expression import and_, or_, desc, not_, distinct, cast, \
    between
from smartnet.impl import DataService
from DataService import Community, GeographicalData, Admin, User, ForgotPassword,\
InterPost, IntraPost, Comment, Message, FileObject
from datetime import datetime
from smartnet.utils.CommunityStatus import CommunityStatus
from sqlalchemy.orm import aliased

DataService.initialize()


def GetCommunities():
    try:
        return Community.query.all()
    finally:
        session.close()

def GetCommunity(CommunityId):
    try:
        community = Community.query.filter_by(id=CommunityId).one()
        return community
    except:
        return None
    finally:
        session.close()
    
def admin_login(obj):
    try:
        return Admin.query.filter_by(email=obj.email, password=obj.password).one()
    except:
        return None
    finally:
        session.close()
def get_list_of_communities(obj):
    try:
        return Community.query.filter_by(created_by=obj.adminId,status=obj.status).all()
    finally:
        session.close()
    

def create_community(communtity_obj):
    geo_data = GeographicalData.query.filter_by(google_place_id = communtity_obj.place_id).all()
    if geo_data:
        return False
    try:
        geo_data = GeographicalData()
        geo_data.google_place_id = communtity_obj.place_id
        geo_data.lat = communtity_obj.lat
        geo_data.lon = communtity_obj.lon
        session.commit()
        obj = Community()
        obj.name = communtity_obj.name
        obj.city = communtity_obj.city
        obj.street_address = communtity_obj.street_address
        obj.state = communtity_obj.state
        obj.country = communtity_obj.country
        obj.zipcode = communtity_obj.zipcode
        obj.created_by = communtity_obj.created_by
        obj.created_timestamp = datetime.now()
        obj.geo_id = geo_data.id
        obj.type = communtity_obj.type
        obj.status = CommunityStatus.pending.value
        session.commit()
        return True
    except:
        return False

def GetUsersByCommunityId(id):
    return User.query.filter_by(community_id=id).order_by((User.first_name)).all()
    
def InsertUser(first_name,last_name,email,password,phone_number,c_id):
    try:
        user = User()
        user.first_name = first_name
        user.last_name = last_name
        user.email = email
        user.password = password
        user.phone_number = phone_number
        user.community_id = int(c_id)
        user.verified = False
        session.commit()
        return True
    except:
        return False

def ResetUserPassword(id,password):
    try:
        user = User.query.filter_by(id=id).one()
        user.password = password
        session.commit()
        return True
    except:
        return False
    finally:
        session.close()

def getUserByEmail(email):
    try:
        exist = User.query.filter_by(email=email).one()
    except:
        return None
    finally:
        session.close()
    return exist

def VerifyEmailPassword(email,password):
    try:
        user = User.query.filter_by(email=email,password=password).one()
        return user
    except Exception as e:
        print(e)
        return None
    finally:
        session.close()

def InsertForgotPassword(ForgotPasswordObj):
    try:
        forgotPassword = ForgotPassword()
        forgotPassword.uuid = ForgotPasswordObj.uuid
        forgotPassword.timestamp = ForgotPasswordObj.timestamp
        forgotPassword.user_id = ForgotPasswordObj.user_id
        forgotPassword.used = ForgotPasswordObj.used
        session.commit()
        return True
    except:
        return False
    
def GetCommunityAndCoordinates():
    x = session.query(Community,GeographicalData).join((GeographicalData,Community.geo_id==GeographicalData.id)).all()
    session.close()
    return x

def GetForgotPassword(uuid):
    try:
        return ForgotPassword.query.filter_by(uuid=uuid).one()
    except:
        return False
    
def GetCommentsByPostId(PostId):
    comments = Comment.query.filter_by(id=PostId).all()
    return comments


def GetCommentsByUserId(UserId):
    comments = Comment.query.filter_by(id=UserId).all()
    return comments

def InsertComment(CommentObj):
    obj = Comment()
    obj.content = CommentObj.content
    obj.user_id = CommentObj.user_id
    obj.post_id = CommentObj.post_id
    session.commit()
    return True,obj.id 
    

def DeleteComment(CommentId):
    try:
        comment = Comment.query.filter_by(id=CommentId).one()
        comment.delete()
        session.commit()
        return True
    except:
        return False

def UpdateComment(CommentId):
    try:
        comment = Comment.query.filter_by(id=CommentId).one()
        comment.content = CommentObj.content
        session.commit()
        return True
    except:
        return False

def GetPosts(PostType="intra"):
    if PostType == "inter":
        return InterPost.query.all().order_by(InterPost.timestamp)
    else:
        return IntraPost.query.all().order_by(InterPost.timestamp)

def GetInterPostsByCommunityId(communityId,category,limit=None,offset=None):
    if offset and limit:
        if category:
            x = session.query(User,InterPost,Community).join((InterPost,User.id==InterPost.user_id)).join((Community,InterPost.community_id==Community.id)).\
            filter(InterPost.community_id==communityId).filter(InterPost.category == category).filter(InterPost.admin_approved==True).order_by(InterPost.timestamp).offset(offset).limit(limit).all()
        else:
            x = session.query(User,InterPost,Community).join((InterPost,User.id==InterPost.user_id)).join((Community,InterPost.community_id==Community.id)).\
            filter(InterPost.community_id==communityId).filter(InterPost.admin_approved==True).order_by(InterPost.timestamp).offset(offset).limit(limit).all()
         
    else:
        x = session.query(InterPost,User,Community).join((InterPost,User.id==IntraPost.user_id)).join((InterPost,Community.id==InterPost.community_id)).\
        filter(InterPost.community_id==communityId).filter(InterPost.admin_approved==True).all()
    session.close()
    return x
    
def get_p_ip(admin_id):
    a = Admin.get_by(id=admin_id)
    x = session.query(InterPost).filter(InterPost.admin_approved==False).filter(InterPost.community_id==a.community_id).order_by(InterPost.timestamp).all()
    session.close()
    return x
    

def GetIntraPostsByCommunityId(communityId,limit=None,offset=None,filter_category=None):
    if offset and limit:
        if filter_category:
            x= session.query(User,IntraPost,Community).join((IntraPost,User.id==IntraPost.user_id)).order_by(desc(IntraPost.timestamp)).join((Community,IntraPost.community_id==Community.id)).\
                filter(IntraPost.community_id==communityId).filter(IntraPost.category == filter_category).offset(offset).limit(limit).all()
                
            session.close()
            return x
        else:
                
            x= session.query(User,IntraPost,Community).join((IntraPost,User.id==IntraPost.user_id)).order_by(desc(IntraPost.timestamp)).join((Community,IntraPost.community_id==Community.id)).\
                filter(IntraPost.community_id==communityId).offset(offset).limit(limit).all()
            
            session.close()
            return x   

def get_file_obj(f_id):
    try:
        return FileObject.get_by(id=f_id)
    finally:
        session.close()

def InsertIntraPost(IntraPostObj):
    if IntraPostObj.image_ids:
        fo = FileObject()
        fo.key = IntraPostObj.image_ids
        session.commit()
    intraPost = IntraPost()
    intraPost.title = IntraPostObj.title
    intraPost.content = IntraPostObj.content
    intraPost.category = IntraPostObj.category
    intraPost.timestamp = IntraPostObj.timestamp
    intraPost.user_id = IntraPostObj.user_id
    intraPost.community_id = IntraPostObj.community_id
    if IntraPostObj.image_ids:
        intraPost.file_id = fo.id
    session.commit()

def InsertInterPost(InterPostObj):
    if InterPostObj.image_ids:
        fo = FileObject()
        fo.key = InterPostObj.image_ids
        session.commit()
    interPost = InterPost()
    interPost.title = InterPostObj.title
    interPost.content = InterPostObj.content
    interPost.category = InterPostObj.category
    interPost.timestamp = InterPostObj.timestamp
    interPost.user_id = InterPostObj.user_id
    interPost.community_id = InterPostObj.community_id
    interPost.admin_approved = InterPostObj.admin_approved
    if InterPostObj.image_ids:
        interPost.file_id = fo.id
    session.commit()

def DeletePost(Id,PostType="intra"):
    try:
        if PostType == "inter":
            interPost = InterPost.query.filter_by(id).one()
            interPost.delete()
            session.commit()
            return True
        else:
            intraPost = IntraPost.query.filter_by(id).one()
            intraPost.delete()
            session.commit()
            return True
    except:
        return False

def UpdateIntraPost(IntraPostObj):
    try:
        intraPostObj = IntraPost.query.filter_by(id=IntraPostObj.id).one()
        intraPostObj.content = IntraPostObj.content
        intraPostObj.title = IntraPostObj.title
        session.commit()
        return True
    except:
        return False

def UpdateInterPost(InterPostObj):
    try:
        interPostObj = InterPost.query.filter_by(id=IntraPostObj.id).one()
        interPostObj.content = InterPostObj.content
        interPostObj.title = InterPostObj.title
        session.commit()
        return True
    except:
        return False

def SendMessage(MessageObj):
    MessageObj = MessageObj.query.filter_by(id=MessageObj.id).one()
    MessageObj.title = MessageObj.title
    MessageObj.content = MessageObj.content
    MessageObj.timestamp = MessageObj.timestamp
    MessageObj.from_user_id= MessageObj.from_user_id
    MessageObj.to_user_id= MessageObj.to_user_id
    session.commit()  

def getUserById(user_id):
    try:
        d_user = User.query.filter_by(id=user_id).one()
        return d_user
    finally:
        session.close()  

def GetCommentsByPostId(postId):
    try:
        return session.query(Comment,User).join((User,Comment.user_id==User.id)).filter(Comment.post_id==postId).all()
    finally:
        session.close()

def InsertMessage(MessageObj):
    try:
        obj = Message()
        obj.title = MessageObj.title
        obj.content = MessageObj.content
        obj.timestamp = datetime.now()
        obj.from_user_id= MessageObj.from_user_id
        obj.to_user_id= MessageObj.to_user_id
        session.commit()
        return True
    except:
        return False

def GetUsersByToUserId(to_user_id):
    try:
        x =  session.query(Message,User).join((User,Message.from_user_id == User.id)).filter(Message.to_user_id == to_user_id).all()
        return x
    finally:
        session.close()

def GetMessages(from_user_id,to_user_id):
    l = [from_user_id, to_user_id]
    x=  session.query(Message).filter(Message.from_user_id.in_(l)).filter(Message.to_user_id.in_(l)).order_by((Message.timestamp)).all()
    session.close()
    return x

def GetCommunitiesByNotId(community_id):
    x = session.query(Community,GeographicalData).join((GeographicalData,Community.geo_id == GeographicalData.id)).filter(Community.id!=community_id).filter(Community.type!=1).all()
    session.close()
    return x

def get_members_count(c_id):
    c = session.query(User).filter_by(community_id=c_id).count()
    session.close() 
    return c

def GetCommunitiesById(community_id):
    x = session.query(Community,GeographicalData).join((GeographicalData,Community.geo_id == GeographicalData.id)).filter(Community.id==community_id).filter(Community.type!=1).all()
    session.close()
    return x

def GetInterPostsByUserId(user_id):
    x = session.query(InterPost,Community).join((Community,InterPost.community_id == Community.id)).filter(InterPost.user_id==user_id).all()
    session.close()
    return x

def GetIntraPostsByUserId(user_id):
    x = session.query(IntraPost,Community).join((Community,IntraPost.community_id == Community.id)).filter(IntraPost.user_id==user_id).all()
    session.close()
    return x

def verify_user(u_id):
    u  =User.get_by(id=u_id)
    u.verified = True
    session.commit()

def assign_c_admin(c_email,c_id):
    d_user = getUserByEmail(c_email)
    if d_user is None:
        return "User doesn't exist"
    if int(c_id)!=d_user.community_id:
        return "User belongs to different community"
    a = Admin()
    a.first_name =d_user.first_name 
    a.last_name =d_user.last_name
    a.password = d_user.password
    a.email = c_email
    a.community_id = d_user.community_id
    a.role = 1
    a.phone_number = d_user.phone_number
    verify_user(d_user.id)
#     x = session.query(Community, GeographicalData).join((GeographicalData, Community.geo_id== GeographicalData.id)).filter(Community.id == int(c_id)).filter(Community.type != 1).all()
#     for c in x:
#         c[0].status=1
    session.commit()
    return "Admin added successfully"

def activate_c(c_id):
    community = Community.query.filter_by(id=c_id).one()
    community.status=1
    session.commit()
    return "Community maked as active"

def get_pending_users(admin_id):
    a = Admin.get_by(id=admin_id)
    u = User.query.filter_by(community_id=a.community_id).filter_by(verified=False).all()
    session.close()
    return u

def markUserAsActive(user_id):
    verify_user(user_id)
    return "User Verified"

def activate_ip(p_id):
    ip = InterPost.get_by(id=p_id)
    ip.admin_approved = True
    session.commit()
    return "Inter Post Approved"
    
    