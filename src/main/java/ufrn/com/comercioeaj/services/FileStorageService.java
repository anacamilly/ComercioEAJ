package ufrn.com.comercioeaj.services;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {

    private static final String UPLOAD_DIR = "src/main/webapp/WEB-INF/images";

    private final Path root = Paths.get(UPLOAD_DIR);

    public FileStorageService() {
        //init();
    }

    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public String save2(MultipartFile file, String imageName) {
        try {
            String filename = imageName + "_" + file.getOriginalFilename();
            Files.copy(file.getInputStream(), this.root.resolve(filename));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return imageName;
    }

    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            // CASO DE ERRO TEM Q COMENTAR A LINHA ABAIXO
            //throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public String saveCroppedImage(byte[] imageData, String imageName) throws IOException {
        String uniqueFileName = generateUniqueFileName(imageName);
        String imagePath = UPLOAD_DIR + File.separator + uniqueFileName;
        FileCopyUtils.copy(imageData, new FileOutputStream(new File(imagePath)));
        return uniqueFileName;
    }

    private String generateUniqueFileName(String imageName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String randomString = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String fileExtension = getFileExtension(imageName);
        return timestamp + "_" + randomString + fileExtension;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1) {
            return fileName.substring(dotIndex);
        }
        return "";
    }

    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

}
