package utils;


import models.Category;
import models.Pet;
import models.Tag;
import models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

@Slf4j
public class TestDataGenerator {
    private static final Random random = new Random();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> PET_NAMES = Arrays.asList(
            "Buddy", "Max", "Charlie", "Luna", "Lucy", "Bailey", "Cooper", "Rocky", "Bear", "Duke"
    );

    private static final List<String> PET_STATUSES = Arrays.asList(
            "available", "pending", "sold"
    );

    private static final List<String> CATEGORY_NAMES = Arrays.asList(
            "Dogs", "Cats", "Birds", "Fish", "Reptiles", "Small Pets"
    );

    private static final List<String> TAG_NAMES = Arrays.asList(
            "Friendly", "Trained", "Young", "Adult", "Vaccinated", "Neutered", "Special Needs", "Playful"
    );

    // Generate a random Pet
    public static Pet generatePet() {
        return Pet.builder()
                .id(generateRandomId())
                .category(generateCategory())
                .name(getRandomElement(PET_NAMES))
                .photoUrls(Collections.singletonList("http://example.com/pet/photo.jpg"))
                .tags(generateTags(random.nextInt(3) + 1))  // 1-3 random tags
                .status(getRandomElement(PET_STATUSES))
                .build();
    }

    // Generate a Pet with specific parameters
    public static Pet generatePet(String name, String status) {
        return Pet.builder()
                .id(generateRandomId())
                .category(generateCategory())
                .name(name)
                .photoUrls(Collections.singletonList("http://example.com/pet/photo.jpg"))
                .tags(generateTags(1))
                .status(status)
                .build();
    }

    // Generate a random Category
    public static Category generateCategory() {
        return Category.builder()
                .id(generateRandomId())
                .name(getRandomElement(CATEGORY_NAMES))
                .build();
    }

    // Generate a random Tag
    public static Tag generateTag() {
        return Tag.builder()
                .id(generateRandomId())
                .name(getRandomElement(TAG_NAMES))
                .build();
    }

    // Generate multiple random Tags
    public static List<Tag> generateTags(int count) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tags.add(generateTag());
        }
        return tags;
    }

    // Generate a random User
    public static User generateUser() {
        String username = "user" + generateRandomId();
        return User.builder()
                .id(generateRandomId())
                .username(username)
                .firstName("Test")
                .lastName("User")
                .email(username + "@example.com")
                .password(generateRandomPassword())
                .phone("1234567890")
                .userStatus(1)
                .build();
    }

    // Helper method to generate random ID
    private static Long generateRandomId() {
        return System.currentTimeMillis() + random.nextInt(1000);
    }

    // Helper method to generate random password
    private static String generateRandomPassword() {
        return UUID.randomUUID().toString().substring(0, 10) + "A1!";
    }

    // Helper method to get random element from a list
    private static <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    // Convert object to JSON string
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error converting object to JSON", e);
            return "{}";
        }
    }

}
