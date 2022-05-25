package com.nst.fitnessu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AwsS3Service {

    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public String uploadImage(Long id, MultipartFile multipartFile) {

        //s3에서 기존 파일 삭제
        deleteImage(id);

        validateFileExists(multipartFile);

        String fileName = buildFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 업로드 실패.");
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private void deleteImage(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException());

        String imageUrl=user.getProfileImage();

        if(imageUrl !=null){
            boolean isExistObject = amazonS3Client.doesObjectExist(bucketName, imageUrl);

            if (isExistObject == true) {
                amazonS3Client.deleteObject(bucketName, imageUrl);
            }
        }
    }

    private String buildFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        int fileExtensionIndex = originalFilename.lastIndexOf(".");
        String fileExtension = originalFilename.substring(fileExtensionIndex);
        return uuid+fileExtension;
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
    }
}