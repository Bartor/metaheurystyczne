main.jar: src/main.kt
	kotlinc src/*.kt src/*/*.kt -Xopt-in=kotlin.ExperimentalUnsignedTypes -Xopt-in=kotlin.ExperimentalStdlibApi -include-runtime -d main.jar && \
	chmod 755 main
	
clean:
	rm main.jar