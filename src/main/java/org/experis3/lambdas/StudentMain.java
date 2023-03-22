package org.experis3.lambdas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Comparator, Anonymous class
 * Functional interfaces
 * Function (map), Predicate (filter, anyMatch, allMatch), Consumer (forEach), Comparator (sorted)
 * Stream
 * Concurrency
 * 
 * https://www.digitalocean.com/community/tutorials/java-8-functional-interfaces
 * https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
 * https://www.oracle.com/webfolder/technetwork/tutorials/moocjdk8/documents/week1/lesson-1-3.pdf
 */
public class StudentMain {
	private static final long SEED = 12345;
	
	public static void main(String[] args) {
		StudentGenerator gen = new StudentGenerator();
		List<Student> students = gen.generate(1000);
		
//		Collections.sort(students, new Comparator<Student>() {
//			@Override
//			public int compare(Student o1, Student o2) {
//				return Float.compare(o1.getAverageScore(), o2.getAverageScore());
//			}
//		});
		
//		Collections.sort(students, (o1, o2) -> {
//			return Float.compare(o1.getAverageScore(), o2.getAverageScore());
//		});
		
		Collections.sort(students, (o1, o2) -> Float.compare(o1.getAverageScore(), o2.getAverageScore()));
		
		
		for (int i = 0; i < students.size(); i++) {
			Student student = students.get(i);
			System.out.println(i + "\t" + student);
		}
		
		// Trovare lo studente più giovane nato a Roma
		Optional<Student> max = students.stream()
				.filter(s -> s.getCityOfBirth().equals("Catanzaro"))
				.max((o1, o2) -> o1.getDateOfBirth().compareTo(o2.getDateOfBirth()));
		
		if (max.isEmpty()) {
			System.out.println("Nessuno studente trovato a Catanzaro");
		} else {
			System.out.println(max.get());
		}	
		
		
		// Stampare in ordine di numero di esami (decrescente) solo
		// gli studenti con 2 o più nazionalità
		List<Student> ss = students.stream()
				.filter(student -> student.getNationalities().size() > 1)
				.sorted((a, b) -> {
					return Integer.compare(b.getNumExams(), a.getNumExams());
				})
				.collect(Collectors.toList());
		
		// Sintassi ::
//		ss.forEach(s -> System.out.println(s));
//		ss.forEach(System.out::println);
		
		students.stream().collect(Collectors.averagingDouble(s -> s.getAverageScore()));

		Map<String, List<Student>> collect = students.stream().collect(Collectors.groupingBy(s -> s.getCityOfBirth()));
		
		// Function<T, K> -> Function<Student, String>
		// Function<T, K> returns Map<K, List<T>>
		Map<String, List<Student>> collect2 = 
			students.stream().collect(Collectors.groupingBy(s -> s.getAverageScore() > 25 ? "Eccellente" : "Sufficiente"));
		
		Map<Integer, Student> studentsById = students
				.stream()
				.collect(Collectors.toMap(s -> s.getId(), s -> s));
		
//		Student student = studentsById.get(452);
		
		
		// Media per città con lambda
		Map<String, Double> mediePerCitta = 
				students.stream()
				.collect(Collectors.groupingBy(s -> s.getCityOfBirth(), Collectors.averagingDouble(s -> s.getAverageScore())));

//		students.stream().collect(Collectors.toMap(null, null))
		
//		Map<String, List<Student>>
		
//		Map<String, Float> medie = mediaPerCitta(students);
//		System.out.println(medie);
//		
//		List<Student> filtrati = filtraPerMedia(students, 22);
//		System.out.println("\n\n=============================================================\n\n");
//		for (int i = 0; i < filtrati.size(); i++) {
//			Student student = filtrati.get(i);
//			System.out.println(i + "\t" + student);
//		}
		
//		Optional<Student> findAny = students.stream()
//			.filter(s -> s.getAverageScore() > 22)
//			.sorted((o1, o2) -> Float.compare(o1.getAverageScore(), o2.getAverageScore()))
//			.findFirst();
		
//		System.out.println(count);
	}
	
	public static List<Student> filtraPerMedia(List<Student> students, float media) {
		List<Student> result = new ArrayList<>();
		
		for (Student s : students) {
			if (s.getAverageScore() > media)
				result.add(s);
		}
		
		return result;
	}
	
	public static Map<String, Float> mediaPerCitta(List<Student> students) {
		Map<String, Float> m = new HashMap<>();
		Map<String, Integer> conteggi = new HashMap<>();
		
		for (Student s : students) {
			String city = s.getCityOfBirth();
			conteggi.putIfAbsent(city, 0);
			conteggi.put(city, conteggi.get(city) + 1);
			
			m.putIfAbsent(city, 0f);
			m.put(city, m.get(city) + s.getAverageScore());
		}
		
		for (String city : m.keySet()) {
			m.put(city, m.get(city) / conteggi.get(city));
		}
		
		return m;
	}
}
