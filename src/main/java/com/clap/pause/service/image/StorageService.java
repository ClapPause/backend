package com.clap.pause.service.image;

import java.io.File;
import java.util.List;

public interface StorageService {
    String uploadImage(File file);

    List<String> uploadImages(List<File> files);
}
