load("@bazel_gazelle//:def.bzl", "gazelle")

# gazelle:prefix github.com/exoson/EEnginev3
gazelle(name = "gazelle")

filegroup(
    name = "cert",
    srcs = ["server.crt"],
    visibility = ["//visibility:public"]
)
