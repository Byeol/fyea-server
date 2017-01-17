package kr.ac.yonsei.fyea.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DataType {
    PERSONAL("PERSONAL"),
    SURVEY("SURVEY"),
    COUNSELING("COUNSELING"),
    GRADE("GRADE");

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
