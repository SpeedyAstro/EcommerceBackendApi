package in.astro.service.impl;

import com.cloudinary.Cloudinary;
import in.astro.service.ICloudinaryImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryImageService {
    @Autowired
    public Cloudinary cloudinary;
    @Override
    public Map<?, ?> uploadImage(MultipartFile file) {
        try {
           return this.cloudinary.uploader().upload(file.getBytes(), Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
