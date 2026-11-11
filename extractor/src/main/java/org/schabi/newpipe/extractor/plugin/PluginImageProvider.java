package org.schabi.newpipe.extractor.plugin;

import java.io.IOException;

import javax.annotation.Nonnull;

/**
 * Implemented by a {@link org.schabi.newpipe.extractor.StreamingService} that can supply the
 * bytes of some of its image urls itself instead of letting the host download them.
 *
 * <p>This is the extension point for images the client cannot decode on its own, such as
 * AES-encrypted cover images. Hosts look up the owning service for every image url and ask
 * it before performing a normal network request.</p>
 *
 * <p>Added in plugin API 1.1.</p>
 */
public interface PluginImageProvider {
    /**
     * Must be cheap, pure and thread safe, and must never throw: hosts call it for every image
     * request with an arbitrary url. Return {@code true} only for urls this provider owns.
     *
     * @param imageUrl the image url about to be loaded
     * @return whether {@link #loadImage(String)} should be called for this url
     */
    boolean handlesImage(@Nonnull String imageUrl);

    /**
     * Loads and decodes an image. May perform network IO.
     *
     * @param imageUrl the url passed to {@link #handlesImage(String)}
     * @return the decoded image
     * @throws IOException when the image cannot be produced; hosts then fall back to a normal
     *                     network request for the url
     */
    @Nonnull
    PluginImage loadImage(@Nonnull String imageUrl) throws IOException;
}
