package com.nst.fitnessu.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nst.fitnessu.domain.Image;
import com.nst.fitnessu.domain.Post;
import com.nst.fitnessu.domain.User;
import com.nst.fitnessu.repository.ImageRepository;
import com.nst.fitnessu.repository.PostRepository;
import com.nst.fitnessu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageService {

    private final AmazonS3Client amazonS3Client;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public void uploadPostImages(Long postId, List<MultipartFile> multipartFiles) {


        if(multipartFiles==null||multipartFiles.isEmpty())
            return;

        Post post= postRepository.findById(postId)
                .orElseThrow(()->new IllegalArgumentException());

        for(MultipartFile multipartFile : multipartFiles){
            if(multipartFile.isEmpty())
                continue;

            Image image=Image.builder()
                    .imageUrl(uploadImage(multipartFile))
                    .build();
            System.out.println("image = sex "+image);
            image.setPost(post);
            imageRepository.save(image);
        }
    }


    @Transactional
    public void deletePostImages(Long id){
        Post post= postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException());
        /*
        for(Image image : post.getImages()){
            deleteImage(image.getImageUrl());
            image.deleteImage(post);
        }

         */

        for(int i=0;i<post.getImages().size();i++){
            Image image=post.getImages().get(i);
            if(image==null){
                continue;
            }else{
                deleteImage(image.getImageUrl());
                image.deleteImage(post);
                i--;
            }
        }
    }

    @Transactional
    public String uploadUserImage(Long id, MultipartFile multipartFile) {

        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException());

        if(multipartFile==null||multipartFile.isEmpty())
            return user.getProfileImage();

        //s3에서 기존 파일 삭제
        deleteImage(user.getProfileImage());

        return uploadImage(multipartFile);
    }

    private String uploadImage(MultipartFile multipartFile) {
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

    private void deleteImage(String imageUrl) {

        if(imageUrl !=null || imageUrl!="https://974s3.s3.ap-northeast-2.amazonaws.com/90acde97-ed9d-425e-bdf0-00d161a550ae.png"){
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