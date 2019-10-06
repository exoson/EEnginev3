java_library(
    name = "game",
    srcs = glob(["*.java"]),
    deps = [
        "//engine/src/main/java/common/game",
    ],
    resources = [
        "//engine/src/main/resources:resources",
    ],
    visibility = ["//visibility:public"],
)

java_binary(
    name = "server",
    srcs = ["Main.java"],
    main_class = "server.game.Main",
    deps = [
        ":game",
        "//engine/src/main/java/client/graphics",
    ],
)
