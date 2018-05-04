package convert;


import io.reactivex.Completable;

/**
 * Classes which implements that interface are used to converting files in special situations based on information contains in {@link Convertable}
 */
public interface ConvertStrategy {
    /**
     * Simple converting file method from some format to another which developer set in {@link Convertable}.
     * @param converter contains information about converting process
     * @return When process succeeded then will be emmit signal complete, error should emmit error with some {@link Exception}.
     */
    Completable convert(Convertable converter);
}
