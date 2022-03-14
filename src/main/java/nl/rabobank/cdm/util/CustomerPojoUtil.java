package nl.rabobank.cdm.util;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CustomerPojoUtil {


	/**
	 * Converts list of source class objects into list of target class objects.
	 *
	 * @param source
	 * @param targetClass
	 * @param modelMapper
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass, ModelMapper modelMapper) {
		return source.stream().map(srcElement -> modelMapper.map(srcElement, targetClass)).collect(Collectors.toList());
	}

}
