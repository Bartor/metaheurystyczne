main.jar: src/main.kt
	kotlinc src/*.kt -include-runtime -d main.jar && \
	chmod 755 main
	
clean:
	rm main.jar