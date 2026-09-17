export interface MockFactory<T> {
  generate(override?: Partial<T>): T;
  reset(): void;
}

export abstract class BaseMockFactory<T> implements MockFactory<T> {
  private seq = 0;

  protected abstract readonly length: number;
  protected abstract build(index: number): T;

  generate(override?: Partial<T>): T {
    if (this.seq >= this.length) {
      this.seq = 0;
    }

    const value = this.build(this.seq);
    this.seq++;

    return { ...value, ...override };
  }

  generateMany(count = this.length): T[] {
    this.reset();
    return Array.from({ length: count }, () => this.generate());
 }

  reset(): void {
    this.seq = 0;
  }
}
