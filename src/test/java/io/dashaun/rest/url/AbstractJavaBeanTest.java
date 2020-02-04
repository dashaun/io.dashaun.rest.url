package io.dashaun.rest.url;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.meanbean.lang.Factory;
import org.meanbean.test.BeanTester;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public abstract class AbstractJavaBeanTest<T> {

    @Test
    public void beanIsSerializable() {
        final T myBean = getBeanInstance();
        final byte[] serializedMyBean = SerializationUtils.serialize((Serializable) myBean);
        @SuppressWarnings("unchecked") final T deserializedMyBean = SerializationUtils.deserialize(serializedMyBean);
        assertEquals(myBean, deserializedMyBean);
    }

    @Test
    public void getterAndSetterCorrectness() throws Exception {
        final BeanTester beanTester = new BeanTester();
        beanTester.getFactoryCollection().addFactory(LocalDateTime.class, new LocalDateTimeFactory());
        beanTester.getFactoryCollection().addFactory(Locale.class, new LocaleFactory());
        beanTester.getFactoryCollection().addFactory(String[].class, new StringArrayFactory());
        beanTester.getFactoryCollection().addFactory(Byte[].class, new ByteArrayFactory());
        beanTester.getFactoryCollection().addFactory(Timestamp.class, new TimestampFactory());
        beanTester.testBean(getBeanInstance().getClass());
    }

    protected abstract T getBeanInstance();

    public class LocalDateTimeFactory implements Factory {
        @Override
        public LocalDateTime create() {
            return LocalDateTime.now();
        }
    }

    public class LocaleFactory implements Factory {
        @Override
        public Locale create() {
            return Locale.US;
        }
    }

    public class StringArrayFactory implements Factory<String[]> {
        @Override
        public String[] create() {
            return new String[]{};
        }

    }

    public class ByteArrayFactory implements Factory<Byte[]> {
        @Override
        public Byte[] create() {
            return new Byte[]{};
        }
    }

    public class TimestampFactory implements Factory<Timestamp> {
        @Override
        public Timestamp create() {
            return new Timestamp(new Date().getTime());
        }
    }

}
