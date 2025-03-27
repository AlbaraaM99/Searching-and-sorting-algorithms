import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class StudentInfoSystem {

    private Database db = new Database();

    /*******************************************************
     *
     *  METHODS THAT NEED IMPROVEMENT
     *
     *******************************************************/

    public List<Subject> sortSubjectsByName() {
        List<Subject> subjectsByName = new ArrayList<>(db.subjects);

        int n = subjectsByName.size();
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                Subject currentReg = subjectsByName.get(j);
                Subject nextReg = subjectsByName.get(j + 1);
                if (currentReg.name.compareTo(nextReg.name) > 0) {
                    subjectsByName.set(j, nextReg);
                    subjectsByName.set(j + 1, currentReg);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
        return subjectsByName;
    }

    public List<Student> sortStudentsByName() {
        List<Student> students = new ArrayList<>(db.students);

        for (int i = 1; i < students.size(); i++) {
            Student key = students.get(i);
            int j = i - 1;
            while (j >= 0 && students.get(j).getName().compareTo(key.getName()) > 0) {
                students.set(j + 1, students.get(j));
                j--;
            }
            students.set(j + 1, key);
        }
        return students;
    }

    public Student findStudent(Integer studentNumber) {
        List<Student> students = this.sortStudentsByNumber();

        int left = 0;
        int right = students.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comp = students.get(mid).studentNumber.compareTo(studentNumber);
            if (comp == 0) return students.get(mid);
            else if (comp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    /*******************************************************
     *
     *  METHODS THAT DO NOT NEED IMPROVEMENT
     *
     *******************************************************/

    public Student findStudent(String name) {
        List<Student> students = this.sortStudentsByName();
        int left = 0;
        int right = students.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int comp = students.get(mid).name.compareTo(name);
            if (comp == 0) return students.get(mid);
            else if (comp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    public List<Exam> findExams(String subjectId) {
        List<Exam> sortedExams = this.sortExamsBySubjectId();
        ArrayList<Exam> foundExams = new ArrayList<>();
        for (Exam exam : sortedExams) {
            if (exam.getSubjectId().equals(subjectId)) {
                foundExams.add(exam);
                if (foundExams.size() == 2) break;
            }
        }
        return foundExams;
    }

    public Exam findExam(int examId, List<Exam> inExams) {
        for (Exam exam : inExams) {
            if (exam.examId == examId) return exam;
        }
        return null;
    }

    public Subject findSubject(String subjectId) {
        List<Subject> subjects = this.sortSubjectsById();
        for (Subject subject : subjects) {
            if (subject.id.equals(subjectId)) return subject;
        }
        return null;
    }

    public List<Student> sortStudentsByNumber() {
        List<Student> studentsById = new ArrayList<>(db.students);
        int n = studentsById.size();
        for (int subArrSize = 1; subArrSize < n; subArrSize *= 2) {
            for (int leftStart = 0; leftStart < n - subArrSize; leftStart += 2 * subArrSize) {
                int rightStart = leftStart + subArrSize;
                int rightEnd = Math.min(leftStart + 2 * subArrSize, n);
                ArrayList<Student> left = new ArrayList<>(studentsById.subList(leftStart, rightStart));
                ArrayList<Student> right = new ArrayList<>(studentsById.subList(rightStart, rightEnd));
                merge(studentsById, leftStart, left, right);
            }
        }
        return studentsById;
    }

    private void merge(List<Student> studentsById, int start, ArrayList<Student> left, ArrayList<Student> right) {
        int i = 0, j = 0, k = start;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).studentNumber <= right.get(j).studentNumber) {
                studentsById.set(k++, left.get(i++));
            } else {
                studentsById.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) {
            studentsById.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            studentsById.set(k++, right.get(j++));
        }
    }

    public List<Subject> sortSubjectsById() {
        TreeMap<String, Subject> subjectMap = new TreeMap<>();
        for (Subject subject : db.subjects) {
            subjectMap.put(subject.id, subject);
        }
        return new ArrayList<>(subjectMap.values());
    }

    public List<Exam> sortExamsBySubjectId() {
        List<Exam> examsToSort = new ArrayList<>(db.exams);
        for (int i = 1; i < examsToSort.size(); i++) {
            Exam key = examsToSort.get(i);
            int j = i - 1;
            while (j >= 0 && examsToSort.get(j).subjectId.compareTo(key.subjectId) > 0) {
                examsToSort.set(j + 1, examsToSort.get(j));
                j--;
            }
            examsToSort.set(j + 1, key);
        }
        return examsToSort;
    }

    public void addStudent(Student student) {
        db.students.add(student);
    }

    public void removeStudent(Student student) {
        db.students.remove(student);
    }

    public int newStudentNumber() {
        List<Student> students = this.sortStudentsByNumber();
        Student lastStudent = students.get(students.size() - 1);
        int lastId = lastStudent.getStudentNumber();
        return lastId + 1;
    }

    public static void main(String[] args) {
        StudentInfoSystem studentInfoSystem = new StudentInfoSystem();

        System.out.println("\n\nSORT SUBJECTS BY NAME\n");
        List<Subject> subjects = studentInfoSystem.sortSubjectsByName();
        for (Subject subject : subjects) {
            System.out.println(subject);
        }

        System.out.println("\n\nSORT STUDENTS BY NAME\n");
        List<Student> students = studentInfoSystem.sortStudentsByName();
        for (Student student : students) {
            System.out.println(student);
        }
    }
}
