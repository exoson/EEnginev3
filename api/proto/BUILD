package(default_visibility = ["//visibility:public"])

load("@io_bazel_rules_go//go:def.bzl", "go_library")
load("@io_bazel_rules_go//proto:def.bzl", "go_proto_library")

proto_library(
    name = "mmserver_proto",
    srcs = ["mmserver.proto"],
)

go_proto_library(
    name = "mmserver_go_proto",
    proto = ":mmserver_proto",
    compilers = ["@io_bazel_rules_go//proto:go_grpc"],
    importpath = "github.com/exoson/EEnginev3/api/proto/mmserver",
)

go_library(
    name = "go_default_library",
    embed = [":mmserver_go_proto"],
    importpath = "github.com/exoson/EEnginev3/api/proto/mmserver",
)
