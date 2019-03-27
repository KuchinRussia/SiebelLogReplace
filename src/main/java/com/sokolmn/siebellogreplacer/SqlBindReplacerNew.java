package com.sokolmn.siebellogreplacer;

import javafx.util.Pair;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlBindReplacerNew {
	private static final String BINDING_STATEMENT = "Bind variable";
	private static final String SELECT_DETECTION = "SELECT";
	private static final char NEW_LINE_CHAR = '\n';
	private static final Pair<String, String> SELECT_REPLACEMENT_PAIR = new Pair(":1", "");

	public static String replace(String source) {
		String sql = getSql(source);
		Deque<Pair<String, String>> bindingsMap = getBindings(source);
		modifyBindingsIfSelect(sql, bindingsMap);
		return applyBindings(sql, bindingsMap);
	}

	private static String getSql(String source) {
		return source.substring(0, findFirstBindPosition(source)).trim();
	}

	private static int findFirstBindPosition(String source) {
		int firstBindPosition = source.indexOf(BINDING_STATEMENT);
		String select = source.substring(0, firstBindPosition);
		return select.lastIndexOf(NEW_LINE_CHAR);
	}

	private static Deque<Pair<String, String>> getBindings(String source) {
		LinkedList<Pair<String, String>> list = getBindingsLinkedList(source);
		Collections.reverse(list);
		return list;
	}

	private static LinkedList<Pair<String, String>> getBindingsLinkedList(String source) {
		return Stream.of(source.split(String.valueOf(NEW_LINE_CHAR)))
				.filter(string -> string.contains(BINDING_STATEMENT))
				.map(SqlBindReplacerNew::getPairFromBinding)
				.collect(Collectors.toCollection(LinkedList::new));
	}

	private static Pair<String, String> getPairFromBinding(String binding) {
		String trimmedBinding = binding.substring(binding.indexOf(BINDING_STATEMENT) + BINDING_STATEMENT.length());
		String[] splittedBinding = trimmedBinding.split(":", 2);
		return new Pair(":" + splittedBinding[0].trim(), splittedBinding[1].trim());
	}

	private static Deque<Pair<String, String>> modifyBindingsIfSelect(String sql, Deque<Pair<String, String>> bindings) {
		if (sql.contains(SELECT_DETECTION)) {
			bindings.removeLast();
			bindings.addLast(SELECT_REPLACEMENT_PAIR);
			return bindings;
		} else {
			return bindings;
		}
	}

	private static String applyBindings(String select, Deque<Pair<String, String>> bindsMap) {
		return bindsMap.stream()
				.map(SqlBindReplacerNew::replaceAll)
				.reduce(Function.identity(), Function::andThen)
				.apply(select);
	}

	private static Function<String, String> replaceAll(Pair<String, String> entry) {
		return string -> string.replaceAll(entry.getKey(), entry.getValue());
	}
}