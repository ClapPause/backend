package com.clap.pause.dto.scrap;

public record ScrapResponse(
        Boolean scraped
) {
    public static ScrapResponse of(boolean scraped) {
        return new ScrapResponse(scraped);
    }
}
