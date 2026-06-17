import { useEffect, useState } from 'react';

type AsyncDataState<T> = {
  data: T | null;
  error: string;
  loading: boolean;
};

export function useAsyncData<T>(load: () => Promise<T>, dependencies: readonly unknown[]) {
  const [state, setState] = useState<AsyncDataState<T>>({
    data: null,
    error: '',
    loading: true,
  });

  useEffect(() => {
    let active = true;


    load()
      .then((data) => {
        if (active) setState({ data, error: '', loading: false });
      })
      .catch((err) => {
        if (active) setState({ data: null, error: (err as Error).message, loading: false });
      });

    return () => {
      active = false;
    };
    // The dependency list is intentionally owned by the calling page, like useEffect.
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, dependencies);

  return state;
}
