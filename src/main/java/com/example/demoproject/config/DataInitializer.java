package com.example.demoproject.config;

import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.PrizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * Data initializer for setting up test data
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PrizeRepository prizeRepository;
    
    @Autowired
    private AuthUserRepository userRepository;
    

    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeUsers();
        initializeLuckyDrawData();
    }

    private void initializeLuckyDrawData() {
        // Create test activity with draw limit per user
        Activity summerDrawActivity = new Activity(
            "Summer Lucky Draw 2024",
            "Amazing prize" +
                    "s for summer!",
            10
        );

        Activity fatherDayDrawActivity = new Activity(
                "Father's day Lucky Draw 2024",
                "Amazing prizes for father's day!",
                50
        );
        Activity saveSummerDrawActivity = activityRepository.save(summerDrawActivity);

        Activity saveFatherDayDrawActivity = activityRepository.save(fatherDayDrawActivity);

        // Create 3 prizes
        Prize iphoneSummerDraw = new Prize("iPhone 15 Pro", "Latest iPhone model", 5, new BigDecimal("0.05"), saveSummerDrawActivity);
        prizeRepository.save(iphoneSummerDraw);

        Prize ipadSummerDraw = new Prize("iPad Pro", "12.9-inch iPad Pro", 10, new BigDecimal("0.10"), saveSummerDrawActivity);
        prizeRepository.save(ipadSummerDraw);

        Prize airpodsSummerDraw = new Prize("AirPods Pro", "Wireless earbuds", 20, new BigDecimal("0.15"), saveSummerDrawActivity);
        prizeRepository.save(airpodsSummerDraw);


        Prize iphoneFatherDay = new Prize("iPhone 15 Pro", "Latest iPhone model", 5, new BigDecimal("0.05"), saveFatherDayDrawActivity);
        prizeRepository.save(iphoneFatherDay);

        Prize ipadFatherDay = new Prize("iPad Pro", "12.9-inch iPad Pro", 10, new BigDecimal("0.10"), saveFatherDayDrawActivity);
        prizeRepository.save(ipadFatherDay);

        Prize airpodsFatherDay = new Prize("AirPods Pro", "Wireless earbuds", 20, new BigDecimal("0.5"), saveFatherDayDrawActivity);
        prizeRepository.save(airpodsFatherDay);


    }
    
    private void initializeUsers() {
        // Create test users
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("User created: admin/admin123 (ROLE_ADMIN)");
        }
        
        if (!userRepository.existsByUsername("user")) {
            User user = new User("user", passwordEncoder.encode("user123"), "ROLE_USER");
            userRepository.save(user);
            System.out.println("User created: user/user123 (ROLE_USER)");
        }
        
        if (!userRepository.existsByUsername("test")) {
            User testUser = new User("test", passwordEncoder.encode("test123"), "ROLE_USER");
            userRepository.save(testUser);
            System.out.println("User created: test/test123 (ROLE_USER)");
        }
        
        System.out.println("Users initialized successfully!");
    }
    

} 