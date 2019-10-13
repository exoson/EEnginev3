package(default_visibility = ["//visibility:public"])

load("@io_bazel_rules_go//go:def.bzl", "go_library")
load("@io_bazel_rules_go//proto:def.bzl", "go_proto_library")

proto_library(
    name = "mmserver_proto",
    srcs = ["mmserver.proto"],
    deps = ["@go_googleapis//google/api:annotations_proto"],
)

go_proto_library(
    name = "mmserver_go_proto",
    compilers = ["@io_bazel_rules_go//proto:go_grpc"],
    importpath = "github.com/exoson/EEnginev3/api/proto/mmserver",
    proto = ":mmserver_proto",
    deps = [
        "@go_googleapis//google/api:annotations_go_proto",
    ],
)

go_proto_library(
    name = "mmserver_gw_go_proto",
    compilers = ["@com_github_grpc_ecosystem_grpc_gateway//protoc-gen-grpc-gateway:go_gen_grpc_gateway"],
    importpath = "github.com/exoson/EEnginev3/api/proto/mmserver_gw",
    proto = ":mmserver_proto",
    embed = [":mmserver_go_proto"],
    deps = [
        ":mmserver_go_proto",
        "@com_github_googleapis_googleapis//google/api:annotations_go_proto",
        "@com_github_golang_protobuf//descriptor:go_default_library",
    ],
)

go_library(
    name = "go_default_library",
    embed = [":mmserver_go_proto"],
    importpath = "github.com/exoson/EEnginev3/api/proto/mmserver",
)
