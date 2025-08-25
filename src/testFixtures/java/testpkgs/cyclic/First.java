package testpkgs.cyclic;

import io.github.abcqwq.magia.injection.annotation.Component;

// Cycle: First -> Second -> Third -> First

@Component
public class First {
    @Component
    public static class Second {
        private final Third third;

        public Second(Third third) {
            this.third = third;
        }
    }

    @Component
    public static class Third {
        private final First first;

        public Third(First first) {
            this.first = first;
        }
    }

    private final Second second;

    public First(Second second) {
        this.second = second;
    }
}
