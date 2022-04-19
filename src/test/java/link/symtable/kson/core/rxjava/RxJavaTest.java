package link.symtable.kson.core.rxjava;

import org.junit.jupiter.api.Test;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaTest {
    @Test
    public void demo1() throws InterruptedException {
        Flowable<String> source = Flowable.fromCallable(() -> {
            Thread.sleep(1000); //  imitate expensive computation
            return "Done";
        });

        Flowable<String> runBackground = source.subscribeOn(Schedulers.io());

        Flowable<String> showForeground = runBackground.observeOn(Schedulers.single());

        showForeground.subscribe(System.out::println, Throwable::printStackTrace);

        Thread.sleep(2000);
    }
}
