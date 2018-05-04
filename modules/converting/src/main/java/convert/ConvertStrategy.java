package convert;


import io.reactivex.Completable;

public interface ConvertStrategy {
    Completable convert(Convertable converter);
}
