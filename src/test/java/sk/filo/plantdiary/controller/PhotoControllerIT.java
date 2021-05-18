package sk.filo.plantdiary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sk.filo.plantdiary.BaseIntegrationTest;
import sk.filo.plantdiary.enums.ExceptionCode;
import sk.filo.plantdiary.service.so.CreatePhotoSO;
import sk.filo.plantdiary.service.so.PhotoSO;
import sk.filo.plantdiary.service.so.PhotoThumbnailSO;
import sk.filo.plantdiary.service.so.UpdatePhotoSO;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PhotoControllerIT extends BaseIntegrationTest {

    @Test
    public void photoTest() throws Exception {
        super.setAuthentication("username");

        // test upload photo on non existing plant
        CreatePhotoSO createPhotoSO = easyRandom.nextObject(CreatePhotoSO.class);
        createPhotoSO.getPlant().setId(100000L);

        File photoFile = new File(getClass().getClassLoader().getResource("photo.jpg").getFile());
        byte[] testFile = Files.readAllBytes(photoFile.toPath());

        MockMultipartFile photo
                = new MockMultipartFile(
                "photo",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                testFile
        );

        MockMultipartFile jsonCreatePhotoSO
                = new MockMultipartFile(
                "createPhotoSO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(createPhotoSO).getBytes()
        );

        mvc.perform(MockMvcRequestBuilders.multipart("/api/photo")
                .file(photo)
                .file(jsonCreatePhotoSO))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // test upload photo on existing plant
        createPhotoSO.getPlant().setId(1L);
        jsonCreatePhotoSO = new MockMultipartFile(
                "createPhotoSO",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                mapToJson(createPhotoSO).getBytes()
        );
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.multipart("/api/photo")
                .file(photo)
                .file(jsonCreatePhotoSO))
                .andExpect(status().isCreated())
                .andReturn();

        PhotoThumbnailSO photoThumbnailSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PhotoThumbnailSO.class);
        assertThat(photoThumbnailSO).usingRecursiveComparison().ignoringFields("id", "thumbnail", "plant.name").isEqualTo(createPhotoSO);
        assertThat(photoThumbnailSO.getId()).isNotNull();
        assertThat(photoThumbnailSO.getThumbnail()).isNotNull();
        assertThat(photoThumbnailSO.getThumbnail()).hasSize(22044);

        // Update photo
        UpdatePhotoSO updatePhotoSO = easyRandom.nextObject(UpdatePhotoSO.class);
        updatePhotoSO.setId(photoThumbnailSO.getId());
        updatePhotoSO.getPlant().setId(1L);
        mvcResult = mvc.perform(MockMvcRequestBuilders.put("/api/photo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updatePhotoSO)))
                .andExpect(status().isOk())
                .andReturn();

        photoThumbnailSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PhotoThumbnailSO.class);
        assertThat(photoThumbnailSO).usingRecursiveComparison().ignoringFields("thumbnail", "plant.name").isEqualTo(updatePhotoSO);

        // Update photo on nonexisting photo
        updatePhotoSO = easyRandom.nextObject(UpdatePhotoSO.class);
        updatePhotoSO.setId(100000L);
        updatePhotoSO.getPlant().setId(1L);
        mvc.perform(MockMvcRequestBuilders.put("/api/photo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updatePhotoSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PHOTO_NOT_FOUND.name()));

        // Update photo on nonexisting plant
        updatePhotoSO = easyRandom.nextObject(UpdatePhotoSO.class);
        updatePhotoSO.setId(photoThumbnailSO.getId());
        updatePhotoSO.getPlant().setId(1000000L);
        mvc.perform(MockMvcRequestBuilders.put("/api/photo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(updatePhotoSO)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        // get one non existing
        mvc.perform(MockMvcRequestBuilders.get("/api/photo/10000"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PHOTO_NOT_FOUND.name()));

        // get one existing
        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/photo/1"))
                .andExpect(status().isOk())
                .andReturn();

        PhotoSO photoSO = mapFromJson(mvcResult.getResponse().getContentAsString(), PhotoSO.class);
        assertThat(photoSO).usingRecursiveComparison().ignoringFields("thumbnail", "data").isEqualTo(photoThumbnailSO);
        assertThat(photoSO.getData()).hasSize(390651);

        // get all by plant id
        mvc.perform(MockMvcRequestBuilders.get("/api/photo/all/100000?page=0&pageSize=10"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PLANT_NOT_FOUND.name()));

        mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/photo/all/1?page=0&pageSize=10"))
                .andExpect(status().isOk())
                .andReturn();

        Page<PhotoThumbnailSO> photos = mapPagedResponse(mvcResult.getResponse().getContentAsString(), PhotoThumbnailSO.class);
        assertThat(photos.getTotalElements()).isEqualTo(1);

        // check delete
        mvc.perform(MockMvcRequestBuilders.delete("/api/photo/" + photoSO.getId()))
                .andExpect(status().isOk());

        // check if deleted
        mvc.perform(MockMvcRequestBuilders.get("/api/photo/" + photoSO.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ExceptionCode.PHOTO_NOT_FOUND.name()));
    }
}
