package com.example.demo.user.Enum;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum TechEnum {
    // Frontend
    REACT("React"),
    VUE_JS("Vue.js"),
    ANGULAR("Angular"),
    NEXT_JS("Next.js"),
    TYPESCRIPT("TypeScript"),
    JAVASCRIPT("JavaScript"),
    HTML("HTML"),
    CSS("CSS"),
    SCSS("SCSS"),
    TAILWIND_CSS("Tailwind CSS"),
    REDUX("Redux"),
    ZUSTAND("Zustand"),

    // Backend
    NODE_JS("Node.js"),
    EXPRESS("Express"),
    SPRING("Spring"),
    DJANGO("Django"),
    FLASK("Flask"),
    NESTJS("NestJS"),
    JAVA("Java"),
    PYTHON("Python"),
    CSHARP("C#"),
    GO("Go"),
    RUBY_ON_RAILS("Ruby on Rails"),
    PHP("PHP"),

    // Database
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    MONGODB("MongoDB"),
    REDIS("Redis"),
    FIREBASE("Firebase"),
    ORACLE("Oracle"),
    SQL_SERVER("SQL Server"),
    DYNAMODB("DynamoDB"),
    ELASTICSEARCH("Elasticsearch"),

    // DevOps
    DOCKER("Docker"),
    KUBERNETES("Kubernetes"),
    AWS("AWS"),
    AZURE("Azure"),
    GCP("GCP"),
    JENKINS("Jenkins"),
    GITHUB_ACTIONS("GitHub Actions"),
    TERRAFORM("Terraform"),
    ANSIBLE("Ansible"),
    PROMETHEUS("Prometheus"),

    // Mobile
    REACT_NATIVE("React Native"),
    FLUTTER("Flutter"),
    SWIFT("Swift"),
    KOTLIN("Kotlin"),
    ANDROID("Android"),
    IOS("iOS"),
    XAMARIN("Xamarin"),
    IONIC("Ionic"),

    // AI
    TENSORFLOW("TensorFlow"),
    PYTORCH("PyTorch"),
    SCIKIT_LEARN("Scikit-learn"),
    OPENCV("OpenCV"),
    NLP("NLP"),
    COMPUTER_VISION("Computer Vision"),
    MACHINE_LEARNING("Machine Learning"),
    DEEP_LEARNING("Deep Learning");

    private final String displayName;

    public String getDisplayName() {
        return displayName;
    }

    // 문자열로 Enum 찾기
    public static TechEnum fromString(String skill) {
        if (skill == null) {
            return null;
        }

        for (TechEnum suggestedSkill : values()) {
            if (suggestedSkill.displayName.equalsIgnoreCase(skill) ||
                    suggestedSkill.name().equalsIgnoreCase(skill.replace(".", "_").replace(" ", "_"))) {
                return suggestedSkill;
            }
        }
        return null;
    }

    // 모든 스킬 이름 리스트로 반환
    public static List<String> getAllSkillNames() {
        return Arrays.stream(values())
                .map(TechEnum::getDisplayName)
                .collect(Collectors.toList());
    }
}
