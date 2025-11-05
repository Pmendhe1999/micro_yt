package com.qc.dto;

import jakarta.validation.constraints.NotNull;

public class MediaDetailsDTO {
    private String description;

    private String mediaFor;

    @NotNull(message = "Media ID is required")
    private Long mediaId;

    private String name;

    private String type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMediaFor() {
        return mediaFor;
    }

    public void setMediaFor(String mediaFor) {
        this.mediaFor = mediaFor;
    }

    public @NotNull(message = "Media ID is required") Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(@NotNull(message = "Media ID is required") Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
