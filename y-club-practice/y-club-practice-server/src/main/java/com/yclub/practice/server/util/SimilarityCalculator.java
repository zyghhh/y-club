package com.yclub.practice.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @desc:
 * @author: zyg
 * @date: 2024-04-29  10:45
 */
public class SimilarityCalculator {
    // 去除HTML标签的正则表达式
    private static final Pattern HTML_TAGS_PATTERN = Pattern.compile("<[^>]+>");

    /**
     * 去除HTML标签并返回纯文本内容
     */
    private static String removeHtmlTags(String htmlContent) {
        return HTML_TAGS_PATTERN.matcher(htmlContent).replaceAll("");
    }

    /**
     * 计算两个文本的余弦相似度
     */
    public static double getSimilarityRatio(String text1, String text2) {
        // 1. 去除HTML标签
        String content1 = removeHtmlTags(text1).toLowerCase();
        String content2 = removeHtmlTags(text2).toLowerCase();

        // 2. 分词处理（这里简化处理，仅做示例，实际应用中可能需要更复杂的分词逻辑）
        String[] words1 = content1.split("\\W+");
        String[] words2 = content2.split("\\W+");

        // 3. 构建词频向量
        Map<String, Integer> freqMap1 = buildFrequencyMap(words1);
        Map<String, Integer> freqMap2 = buildFrequencyMap(words2);

        // 4. 计算词频向量的点积
        double dotProduct = 0.0;
        for (String word : freqMap1.keySet()) {
            if (freqMap2.containsKey(word)) {
                dotProduct += freqMap1.get(word) * freqMap2.get(word);
            }
        }

        // 5. 计算向量的模（长度）
        double magnitude1 = sqrtMagnitude(freqMap1);
        double magnitude2 = sqrtMagnitude(freqMap2);

        // 6. 计算余弦相似度
        return dotProduct / (magnitude1 * magnitude2);
    }

    private static Map<String, Integer> buildFrequencyMap(String[] words) {
        Map<String, Integer> freqMap = new HashMap<>();
        for (String word : words) {
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
        return freqMap;
    }

    private static double sqrtMagnitude(Map<String, Integer> freqMap) {
        double sum = 0.0;
        for (int freq : freqMap.values()) {
            sum += Math.pow(freq, 2);
        }
        return Math.sqrt(sum);
    }
}
