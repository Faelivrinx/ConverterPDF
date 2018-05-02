package converter.convert;

import io.reactivex.Completable;

public interface ConvertStrategy {
    Completable convert(Converter converter);
}
