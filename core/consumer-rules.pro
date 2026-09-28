# ---- Moshi ----
# Codegen adapters call properties directly (no reflection), but keep the annotated model
# shape stable so the generated *JsonAdapter stays in sync with the R8-renamed class.
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
-keep class **JsonAdapter { *; }
-dontwarn com.squareup.moshi.**

# ---- Retrofit / OkHttp ----
-keepattributes Signature, Exceptions, *Annotation*, InnerClasses, EnclosingMethod
-keep interface com.movieapp.core.data.remote.api.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn javax.annotation.**

# ---- Room / SQLCipher ----
-keep class com.movieapp.core.data.local.MovieDatabase { *; }
-keep @androidx.room.Entity class * { *; }
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**
