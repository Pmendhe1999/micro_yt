package com.identity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaDetailsDTO {


    @NotBlank(message = "Media name is required")
    private String name;

    @NotBlank(message = "Media type is required")
    private String type;

    private String description;

    private String mediaFor;

    @NotNull(message = "Media ID is required")
    private Long mediaId;

    @NotNull(message = "User ID is required")
    private Long userId;

    public @NotBlank(message = "Media name is required") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Media name is required") String name) {
        this.name = name;
    }

    public @NotBlank(message = "Media type is required") String getType() {
        return type;
    }

    public void setType(@NotBlank(message = "Media type is required") String type) {
        this.type = type;
    }

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

    public @NotNull(message = "User ID is required") Long getUserId() {
        return userId;
    }

    public void setUserId(@NotNull(message = "User ID is required") Long userId) {
        this.userId = userId;
    }
}
