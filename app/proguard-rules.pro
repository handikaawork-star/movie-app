# Module-specific rules (Moshi, Retrofit, Room, SQLCipher) live in core/consumer-rules.pro
# and are merged in automatically via consumerProguardFiles.

# ---- Koin ----
# Koin builds instances via direct lambda calls (compiled bytecode, not reflection),
# so only the library's own classes need protecting here.
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# ---- LeakCanary ----
# debugImplementation only; harmless if this file is ever shared with a debug config.
-dontwarn com.squareup.leakcanary.**
