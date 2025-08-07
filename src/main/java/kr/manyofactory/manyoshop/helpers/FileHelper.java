package kr.manyofactory.manyoshop.helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.manyofactory.manyoshop.models.UploadItem;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

@Component
@Slf4j
public class FileHelper {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.url}")
    private String uploadUrl;

    @Value("${thumbnail.width}")
    private int thumbnailWidth;

    @Value("${thumbnail.height}")
    private int thumbnailHeight;

    @Value("${thumbnail.crop}")
    private boolean thumbnailCrop;

    public void write(String filePath, byte[] data) throws IOException {
        try (OutputStream os = new FileOutputStream(filePath)) {
            os.write(data);
        }
    }

    public byte[] read(String filePath) throws IOException {
        byte[] data;
        try (InputStream is = new FileInputStream(filePath)) {
            data = new byte[is.available()];
            is.read(data);
        }
        return data;
    }

    public void writeString(String filePath, String content) throws Exception {
        this.write(filePath, content.getBytes("utf-8"));
    }

    public String readString(String filePath) throws Exception {
        byte[] data = read(filePath);
        return new String(data, "utf-8");
    }

    public UploadItem saveMultipartFile(MultipartFile multipartFile) throws Exception {
        String originName = multipartFile.getOriginalFilename();
        if (originName == null || originName.isEmpty()) {
            throw new NullPointerException("업로드 된 파일이 없습니다.");
        }

        Calendar c = Calendar.getInstance();
        String targetDir = String.format("%s/%04d/%02d/%02d", uploadDir, c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        File dir = new File(targetDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String ext = originName.substring(originName.lastIndexOf("."));
        String fileName;
        File targetFile;
        int count = 0;

        while (true) {
            fileName = String.format("%d%d%s", System.currentTimeMillis(), count, ext);
            targetFile = new File(targetDir, fileName);
            if (!targetFile.exists())
                break;
            count++;
        }

        multipartFile.transferTo(targetFile);

        String absPath = targetFile.getAbsolutePath().replace("\\", "/");
        String normalizedUploadDir = new File(uploadDir).getAbsolutePath().replace("\\", "/");
        String filePath = absPath.replace(normalizedUploadDir, "");
        if (!filePath.startsWith("/"))
            filePath = "/" + filePath;
        String fileUrl = uploadUrl + filePath;

        UploadItem item = new UploadItem();
        item.setContentType(multipartFile.getContentType());
        item.setFieldName(multipartFile.getName());
        item.setFileSize(multipartFile.getSize());
        item.setOriginName(originName);
        item.setFilePath(filePath);
        item.setFileUrl(fileUrl);

        if (item.getContentType() != null && item.getContentType().contains("image")) {
            try {
                String thumbnailPath = this.createThumbnail(filePath, thumbnailWidth,
                        thumbnailHeight, thumbnailCrop);
                String thumbnailUrl = uploadUrl + thumbnailPath;
                item.setThumbnailPath(thumbnailPath);
                item.setThumbnailUrl(thumbnailUrl);
            } catch (Exception e) {
                log.error("썸네일 생성에 실패했습니다.", e);
            }
        }

        log.debug(item.toString());
        return item;
    }

    public List<UploadItem> saveMultipartFile(MultipartFile[] uploadFiles) throws Exception {
        if (uploadFiles.length < 1) {
            throw new NullPointerException("업로드 된 파일이 없습니다.");
        }

        List<UploadItem> uploadList = new ArrayList<>();
        for (int i = 0; i < uploadFiles.length; i++) {
            try {
                UploadItem item = this.saveMultipartFile(uploadFiles[i]);
                uploadList.add(item);
            } catch (Exception e) {
                log.error(String.format("%d번째 항목 저장 실패: %s", i, e.getMessage()));
            }
        }

        if (uploadList.isEmpty()) {
            throw new Exception("파일 업로드 실패");
        }

        return uploadList;
    }

    public String createThumbnail(String path, int width, int height, boolean crop)
            throws Exception {
        log.debug(String.format("[Thumbnail] path: %s, size: %dx%d, crop: %s", path, width, height,
                crop));

        File loadFile = new File(this.uploadDir, path);
        String dirPath = loadFile.getParent();
        String fileName = loadFile.getName();
        int p = fileName.lastIndexOf(".");
        String name = fileName.substring(0, p);
        String ext = fileName.substring(p + 1);
        String thumbName = name + "_" + width + "x" + height + "." + ext;
        File f = new File(dirPath, thumbName);
        String saveFile = f.getAbsolutePath().replace("\\", "/");

        log.debug(String.format("[Thumbnail] saveFile: %s", saveFile));

        if (!f.exists()) {
            Builder<File> builder = Thumbnails.of(loadFile);
            if (crop) {
                builder.crop(Positions.CENTER);
            }
            builder.size(width, height);
            builder.useExifOrientation(true);
            builder.outputFormat(ext);
            builder.toFile(saveFile);
        }

        String normalizedUploadDir = new File(uploadDir).getAbsolutePath().replace("\\", "/");
        String thumbnailPath = saveFile.replace(normalizedUploadDir, "");
        if (!thumbnailPath.startsWith("/")) {
            thumbnailPath = "/" + thumbnailPath;
        }

        return thumbnailPath;
    }

    public String getFileUrl(String filePath) throws Exception {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        File file = new File(uploadDir, filePath);
        if (!file.exists()) {
            return null;
        }

        String fileUrl = String.format("%s%s", uploadUrl, filePath);
        return fileUrl;
    }

    public void deleteFile(String filePath) throws Exception {
        File file = new File(uploadDir, filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다." + filePath);
        }
        if (!file.delete()) {
            throw new IOException("파일 삭제에 실패했습니다:" + filePath);
        }
        log.debug("파일 삭제 성공: {}", filePath);

        /** 2) 저장될 썸네일 이미지의 경로 문자열 만들기 */
        String dirPath = file.getParent();
        String fileName = file.getName();
        int p = fileName.lastIndexOf(".");
        String name = fileName.substring(0, p);
        String ext = fileName.substring(p + 1);
        String thumbName = name + "_" + thumbnailWidth + "x" + thumbnailHeight + "." + ext;

        File thumbFile = new File(dirPath, thumbName);
        String thumbPath = thumbFile.getAbsolutePath();

        if (thumbFile.exists()) {
            thumbFile.delete();
            log.debug("썸네일 삭제 성공: {}", thumbPath);
        }

    }
}
