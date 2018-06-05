import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class BaseDaoImpl<T> {
    private Class<T> tClass = null;

    public BaseDaoImpl() throws IllegalAccessException, InstantiationException {
        Class clz = this.getClass();
        ParameterizedType type = (ParameterizedType) clz.getGenericSuperclass();
        System.out.println(type);
        Type[] types = type.getActualTypeArguments();

        tClass = (Class<T>) types[0];
        System.out.println(tClass.getSimpleName());
        System.out.println(tClass.newInstance() instanceof Person);
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        new BaseDaoImpl<Person>();
    }

}
