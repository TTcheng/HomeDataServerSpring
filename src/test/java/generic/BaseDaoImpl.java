package generic;

import entity.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseDaoImpl<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseDaoImpl.class);

    private BaseDaoImpl() throws IllegalAccessException, InstantiationException {
        Class clz = this.getClass();
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        LOGGER.debug("Type : {}",type);
        Type[] types = type.getActualTypeArguments();

        @SuppressWarnings("unchecked")
        Class<T> tClass = (Class<T>) types[0];
        LOGGER.debug(tClass.getSimpleName());
        boolean b = tClass.newInstance() instanceof Person;
        LOGGER.debug("Results:{}", b);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        new BaseDaoImpl<Person>();
    }

}
