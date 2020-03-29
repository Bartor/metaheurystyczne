main.jar: src/main.kt
	kotlinc src/*.kt -include-runtime -d main.jar && \
	chmod 755 main.sh
	
clean:
	rm main.jar