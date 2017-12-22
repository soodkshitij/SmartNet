package com.smartnet.grpc;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.7.0)",
    comments = "Source: smartnet.proto")
public final class SmartnetGrpc {

  private SmartnetGrpc() {}

  public static final String SERVICE_NAME = "smartnet.Smartnet";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.smartnet.grpc.SmartnetOuterClass.Community,
      com.smartnet.grpc.SmartnetOuterClass.BooleanResponse> METHOD_CREATE_COMMUNITY =
      io.grpc.MethodDescriptor.<com.smartnet.grpc.SmartnetOuterClass.Community, com.smartnet.grpc.SmartnetOuterClass.BooleanResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "smartnet.Smartnet", "createCommunity"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.Community.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.BooleanResponse.getDefaultInstance()))
          .setSchemaDescriptor(new SmartnetMethodDescriptorSupplier("createCommunity"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.smartnet.grpc.SmartnetOuterClass.IntegerRequest,
      com.smartnet.grpc.SmartnetOuterClass.Community> METHOD_GET_COMMUNITY_BY_ID =
      io.grpc.MethodDescriptor.<com.smartnet.grpc.SmartnetOuterClass.IntegerRequest, com.smartnet.grpc.SmartnetOuterClass.Community>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "smartnet.Smartnet", "getCommunityById"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.IntegerRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.Community.getDefaultInstance()))
          .setSchemaDescriptor(new SmartnetMethodDescriptorSupplier("getCommunityById"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest,
      com.smartnet.grpc.SmartnetOuterClass.CommunityList> METHOD_GET_LIST_OF_COMMUNITIES =
      io.grpc.MethodDescriptor.<com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest, com.smartnet.grpc.SmartnetOuterClass.CommunityList>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "smartnet.Smartnet", "getListOfCommunities"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.CommunityList.getDefaultInstance()))
          .setSchemaDescriptor(new SmartnetMethodDescriptorSupplier("getListOfCommunities"))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<com.smartnet.grpc.SmartnetOuterClass.Admin,
      com.smartnet.grpc.SmartnetOuterClass.Admin> METHOD_ADMIN_LOGIN =
      io.grpc.MethodDescriptor.<com.smartnet.grpc.SmartnetOuterClass.Admin, com.smartnet.grpc.SmartnetOuterClass.Admin>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "smartnet.Smartnet", "adminLogin"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.Admin.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              com.smartnet.grpc.SmartnetOuterClass.Admin.getDefaultInstance()))
          .setSchemaDescriptor(new SmartnetMethodDescriptorSupplier("adminLogin"))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SmartnetStub newStub(io.grpc.Channel channel) {
    return new SmartnetStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SmartnetBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new SmartnetBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SmartnetFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new SmartnetFutureStub(channel);
  }

  /**
   */
  public static abstract class SmartnetImplBase implements io.grpc.BindableService {

    /**
     */
    public void createCommunity(com.smartnet.grpc.SmartnetOuterClass.Community request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.BooleanResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_CREATE_COMMUNITY, responseObserver);
    }

    /**
     */
    public void getCommunityById(com.smartnet.grpc.SmartnetOuterClass.IntegerRequest request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.Community> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_COMMUNITY_BY_ID, responseObserver);
    }

    /**
     */
    public void getListOfCommunities(com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.CommunityList> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_LIST_OF_COMMUNITIES, responseObserver);
    }

    /**
     */
    public void adminLogin(com.smartnet.grpc.SmartnetOuterClass.Admin request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.Admin> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_ADMIN_LOGIN, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_CREATE_COMMUNITY,
            asyncUnaryCall(
              new MethodHandlers<
                com.smartnet.grpc.SmartnetOuterClass.Community,
                com.smartnet.grpc.SmartnetOuterClass.BooleanResponse>(
                  this, METHODID_CREATE_COMMUNITY)))
          .addMethod(
            METHOD_GET_COMMUNITY_BY_ID,
            asyncUnaryCall(
              new MethodHandlers<
                com.smartnet.grpc.SmartnetOuterClass.IntegerRequest,
                com.smartnet.grpc.SmartnetOuterClass.Community>(
                  this, METHODID_GET_COMMUNITY_BY_ID)))
          .addMethod(
            METHOD_GET_LIST_OF_COMMUNITIES,
            asyncUnaryCall(
              new MethodHandlers<
                com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest,
                com.smartnet.grpc.SmartnetOuterClass.CommunityList>(
                  this, METHODID_GET_LIST_OF_COMMUNITIES)))
          .addMethod(
            METHOD_ADMIN_LOGIN,
            asyncUnaryCall(
              new MethodHandlers<
                com.smartnet.grpc.SmartnetOuterClass.Admin,
                com.smartnet.grpc.SmartnetOuterClass.Admin>(
                  this, METHODID_ADMIN_LOGIN)))
          .build();
    }
  }

  /**
   */
  public static final class SmartnetStub extends io.grpc.stub.AbstractStub<SmartnetStub> {
    private SmartnetStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SmartnetStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartnetStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SmartnetStub(channel, callOptions);
    }

    /**
     */
    public void createCommunity(com.smartnet.grpc.SmartnetOuterClass.Community request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.BooleanResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_CREATE_COMMUNITY, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getCommunityById(com.smartnet.grpc.SmartnetOuterClass.IntegerRequest request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.Community> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_COMMUNITY_BY_ID, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getListOfCommunities(com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.CommunityList> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_LIST_OF_COMMUNITIES, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void adminLogin(com.smartnet.grpc.SmartnetOuterClass.Admin request,
        io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.Admin> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_ADMIN_LOGIN, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class SmartnetBlockingStub extends io.grpc.stub.AbstractStub<SmartnetBlockingStub> {
    private SmartnetBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SmartnetBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartnetBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SmartnetBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.smartnet.grpc.SmartnetOuterClass.BooleanResponse createCommunity(com.smartnet.grpc.SmartnetOuterClass.Community request) {
      return blockingUnaryCall(
          getChannel(), METHOD_CREATE_COMMUNITY, getCallOptions(), request);
    }

    /**
     */
    public com.smartnet.grpc.SmartnetOuterClass.Community getCommunityById(com.smartnet.grpc.SmartnetOuterClass.IntegerRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_COMMUNITY_BY_ID, getCallOptions(), request);
    }

    /**
     */
    public com.smartnet.grpc.SmartnetOuterClass.CommunityList getListOfCommunities(com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_LIST_OF_COMMUNITIES, getCallOptions(), request);
    }

    /**
     */
    public com.smartnet.grpc.SmartnetOuterClass.Admin adminLogin(com.smartnet.grpc.SmartnetOuterClass.Admin request) {
      return blockingUnaryCall(
          getChannel(), METHOD_ADMIN_LOGIN, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class SmartnetFutureStub extends io.grpc.stub.AbstractStub<SmartnetFutureStub> {
    private SmartnetFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private SmartnetFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SmartnetFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new SmartnetFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartnet.grpc.SmartnetOuterClass.BooleanResponse> createCommunity(
        com.smartnet.grpc.SmartnetOuterClass.Community request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_CREATE_COMMUNITY, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartnet.grpc.SmartnetOuterClass.Community> getCommunityById(
        com.smartnet.grpc.SmartnetOuterClass.IntegerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_COMMUNITY_BY_ID, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartnet.grpc.SmartnetOuterClass.CommunityList> getListOfCommunities(
        com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_LIST_OF_COMMUNITIES, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartnet.grpc.SmartnetOuterClass.Admin> adminLogin(
        com.smartnet.grpc.SmartnetOuterClass.Admin request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_ADMIN_LOGIN, getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_COMMUNITY = 0;
  private static final int METHODID_GET_COMMUNITY_BY_ID = 1;
  private static final int METHODID_GET_LIST_OF_COMMUNITIES = 2;
  private static final int METHODID_ADMIN_LOGIN = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final SmartnetImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(SmartnetImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_COMMUNITY:
          serviceImpl.createCommunity((com.smartnet.grpc.SmartnetOuterClass.Community) request,
              (io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.BooleanResponse>) responseObserver);
          break;
        case METHODID_GET_COMMUNITY_BY_ID:
          serviceImpl.getCommunityById((com.smartnet.grpc.SmartnetOuterClass.IntegerRequest) request,
              (io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.Community>) responseObserver);
          break;
        case METHODID_GET_LIST_OF_COMMUNITIES:
          serviceImpl.getListOfCommunities((com.smartnet.grpc.SmartnetOuterClass.CommunityListRequest) request,
              (io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.CommunityList>) responseObserver);
          break;
        case METHODID_ADMIN_LOGIN:
          serviceImpl.adminLogin((com.smartnet.grpc.SmartnetOuterClass.Admin) request,
              (io.grpc.stub.StreamObserver<com.smartnet.grpc.SmartnetOuterClass.Admin>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class SmartnetBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SmartnetBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.smartnet.grpc.SmartnetOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Smartnet");
    }
  }

  private static final class SmartnetFileDescriptorSupplier
      extends SmartnetBaseDescriptorSupplier {
    SmartnetFileDescriptorSupplier() {}
  }

  private static final class SmartnetMethodDescriptorSupplier
      extends SmartnetBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    SmartnetMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SmartnetGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SmartnetFileDescriptorSupplier())
              .addMethod(METHOD_CREATE_COMMUNITY)
              .addMethod(METHOD_GET_COMMUNITY_BY_ID)
              .addMethod(METHOD_GET_LIST_OF_COMMUNITIES)
              .addMethod(METHOD_ADMIN_LOGIN)
              .build();
        }
      }
    }
    return result;
  }
}
