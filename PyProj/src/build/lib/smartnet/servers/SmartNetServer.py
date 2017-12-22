'''
################################## server.py #############################
'''
import time
import grpc
from concurrent import futures
from smartnet.grpcpy import smartnet_pb2_grpc
from smartnet.impl.GrpcHandler import GrpcHandler
import sys
_ONE_DAY_IN_SECONDS = 24*60*60
x =  (sys.path)

def run(host, port):
    '''
    Run the GRPC server
    '''
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=1))
    smartnet_pb2_grpc.add_SmartnetServicer_to_server(GrpcHandler(), server)
    server.add_insecure_port('%s:%d' % (host, port))
    server.start()

    try:
        while True:
            print("Server started at...%d" % port)
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)


if __name__ == '__main__':
    run('0.0.0.0', 3000)
