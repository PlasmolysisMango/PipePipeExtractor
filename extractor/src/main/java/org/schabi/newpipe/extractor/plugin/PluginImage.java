package org.schabi.newpipe.extractor.plugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * An image produced by a plugin from bytes that are not directly reachable by the client,
 * for example an encrypted cover image that the plugin decrypted.
 *
 * <p>Instances are immutable. The data is copied on construction and on every
 * {@link #getData()} call, so plugins may reuse their buffers.</p>
 */
public final class PluginImage {
    public static final int DEFAULT_MAX_AGE_SECONDS = 3600;
    /** Per-image cap; large enough for covers, small enough that a plugin cannot flood memory. */
    public static final int MAX_DATA_BYTES = 8 * 1024 * 1024;

    // Raster formats only: the host decodes these immediately, and SVG could carry script.
    private static final Pattern MEDIA_TYPE_PATTERN =
            Pattern.compile("^image/(png|jpeg|gif|webp|avif|bmp)$");

    @Nonnull
    private final byte[] data;
    @Nonnull
    private final String mediaType;
    private final int maxAgeSeconds;

    public PluginImage(@Nonnull final byte[] data, @Nonnull final String mediaType) {
        this(data, mediaType, DEFAULT_MAX_AGE_SECONDS);
    }

    public PluginImage(@Nonnull final byte[] data,
                       @Nonnull final String mediaType,
                       final int maxAgeSeconds) {
        Objects.requireNonNull(data, "data");
        Objects.requireNonNull(mediaType, "mediaType");
        if (data.length == 0) {
            throw new IllegalArgumentException("Image data is empty");
        }
        if (data.length > MAX_DATA_BYTES) {
            throw new IllegalArgumentException("Image data exceeds " + MAX_DATA_BYTES + " bytes");
        }
        if (!MEDIA_TYPE_PATTERN.matcher(mediaType).matches()) {
            throw new IllegalArgumentException("Invalid image media type: " + mediaType);
        }
        if (maxAgeSeconds < 0) {
            throw new IllegalArgumentException("maxAgeSeconds is negative");
        }
        this.data = Arrays.copyOf(data, data.length);
        this.mediaType = mediaType;
        this.maxAgeSeconds = maxAgeSeconds;
    }

    @Nonnull
    public byte[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    @Nonnull
    public String getMediaType() {
        return mediaType;
    }

    /**
     * @return how long the host may cache this image, in seconds
     */
    public int getMaxAgeSeconds() {
        return maxAgeSeconds;
    }
}
