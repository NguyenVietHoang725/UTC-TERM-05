public class StudentTST extends VietnameseTST<VietnameseTST<Double>> {
    /**
     * Thêm điểm cho một môn của sinh viên.
     */
    public void putStudent(String studentId, String subject, Double grade) {
        VietnameseTST<Double> grades = get(studentId);
        if (grades == null) {
            grades = new VietnameseTST<>();
            put(studentId, grades);
        }
        grades.put(subject, grade);
    }

    /**
     * Lấy điểm của một môn cho sinh viên.
     */
    public Double getGrade(String studentId, String subject) {
        VietnameseTST<Double> grades = get(studentId);
        if (grades == null) return null;
        return grades.get(subject);
    }

    /**
     * Lấy danh sách tất cả mã sinh viên.
     */
    public Iterable<String> getAllStudents() {
        return keys();
    }

    /**
     * Lấy bảng điểm của một sinh viên.
     */
    public Iterable<String> getGrades(String studentId) {
        VietnameseTST<Double> grades = get(studentId);
        if (grades == null) return new Queue<String>();
        return grades.keys();
    }
}