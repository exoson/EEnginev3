load("@io_bazel_rules_go//go:def.bzl", "go_binary", "go_library")

go_library(
    name = "go_default_library",
    srcs = ["main.go"],
    data = [
        "//:cert",
    ],
    importpath = "github.com/exoson/EEnginev3/go/cmd/mmgateway",
    visibility = ["//visibility:private"],
    deps = [
        "//api/proto:mmserver_gw_go_proto",
        "@com_github_golang_glog//:go_default_library",
        "@com_github_grpc_ecosystem_grpc_gateway//runtime:go_default_library",
        "@org_golang_google_grpc//:go_default_library",
        "@org_golang_google_grpc//credentials:go_default_library",
    ],
)

go_binary(
    name = "mmgateway",
    embed = [":go_default_library"],
    visibility = ["//visibility:public"],
)
