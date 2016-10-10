public class TestStringNorm {
	public static void main(String[] args) {
		String str = "abaabababaab";
		char[] x = str.toCharArray();
		int n = x.length;
		int[] B = new int[n];

		B[0] = 0;
		for (int i = 0; i < n - 1; i++) {
			int b = B[i];
			while (b > 0 && x[i + 1] != x[b]) {
				b = B[b];
			}
			if (x[i + 1] == x[b]) {
				B[i] = b;
			} else {
				B[i] = 0;
			}
		}
		for (int b : B) {
			System.out.print(b);
			System.out.print(' ');
		}
	}
}
