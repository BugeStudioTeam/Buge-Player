# Buge Player release rules.
# Media3 is referenced through typed APIs; retain its session service for manifest discovery.
-keep class com.buge.player.media.PlaybackService { *; }
-keep class androidx.media3.session.** { *; }
-dontwarn org.checkerframework.**
