from smartnet.grpcpy import smartnet_pb2_grpc, smartnet_pb2
from smartnet.impl.DataAccessor import create_community, admin_login, get_list_of_communities
from elixir import session
from smartnet.utils.utils import to_java_date

class GrpcHandler(smartnet_pb2_grpc.SmartnetServicer):
    
    
    def createCommunity(self, request, context):
        res = create_community(request)
        return smartnet_pb2.BooleanResponse(result=res)
    
    def getCommunityById(self, request, context):
        pass
    
    def getListOfCommunities(self, request, context):
        community_list = get_list_of_communities(request)
        l = smartnet_pb2.CommunityList()
        r = []
        for c in community_list:
            r.append(smartnet_pb2.Community(id=c.id,name=c.name, city=c.city ,type= c.type ,state=c.state, created_timestamp=to_java_date(c.created_timestamp)))
        l.community.extend(r)
        return l
    
    def adminLogin(self,request, context):
        try:
            res = admin_login(request)
            if not res:
                raise
            return smartnet_pb2.Admin(id=res.id, role=res.role, email = res.email, first_name = res.first_name, last_name = res.last_name)
        finally:
            session.close()