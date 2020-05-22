const readline = require('readline');
const fs = require('fs');

const STOP_RATIO = 0.1;
const TOURNAMENT = 5;
const POPULATION = 20;

Math.randInt = (max) => {
    return Math.floor(Math.random() * max);
}

const dict = fs.readFileSync('dict.txt').toString().split('\r\n');

function shuffleArray(array) {
    const res = array.slice();
    for (let i = 0; i < array.length; i++) {
        const changeWith = Math.randInt(array.length);
        [res[i], res[changeWith]] = [res[changeWith], res[i]];
    }
    return res;
}

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout,
    prompt: ''
});

function MapWithDefault(defaultFn) {
    this.dict = {};
    this.get = function (key) {
        return this.dict[key] ? this.dict[key] : this.dict[key] = defaultFn();
    }
}

let lineNo = 0;
let t, n, s;
let initialPopulation = [];
let letters = new MapWithDefault(() => ({
    value: 0,
    number: 0
}));

rl.prompt();
rl.on('line', line => {
    if (lineNo++ === 0) {
        [t, n, s] = line.split(' ').map(Number.parseFloat);
    } else if (lineNo < n + 2) {
        let [l, v] = line.split(' ');
        letters.get(l).value = Number.parseFloat(v);
        letters.get(l).number++;
    } else {
        initialPopulation.push(line);
        if (lineNo === n + s + 1) {
            rl.close();
            main();
        }
    }
});

function selection(fitnessArray) {
    let best = Math.randInt(fitnessArray.length);
    for (let i = 0; i < TOURNAMENT; i++) {
        let current = Math.randInt(fitnessArray.length);
        if (fitnessArray[current] > fitnessArray[best]) best = current;
    }
    return best;
}

function compareTime(a, b) {
    return a[0] > b[0] || a[1] > b[1];
}

function mutate(word) {
    const availableLetters = shuffleArray(Object.keys(letters.dict));
    const splitIndex = Math.randInt(word.length);

    switch (Math.randInt(5)) {
        case 0:
            return word + availableLetters.filter(e => Math.random() < 0.15).join('');
        case 1:
            return word.split('').map(e => Math.random() < 0.15 ? availableLetters[Math.randInt(availableLetters.length)] : e).join('');
        case 2:
            return word.slice(splitIndex) + word.slice(0, splitIndex);
        case 3:
            return word.slice(0, splitIndex) + availableLetters.filter(e => Math.random() < 0.3).join('');
        case 4:
            return availableLetters.filter(e => Math.random() < 0.3).join('') + word.slice(splitIndex);
    }
}

function crossover(x1, x2) {
    const [i1, i2] = [Math.randInt(x1.length), Math.randInt(x2.length)];

    const [r1, r2] = [x1.slice(i1) + x2.slice(0, i2), x2.slice(i2) + x1.slice(0, i1)];

    for (let i = 0; i < Math.min(r1.length, r2.length); i++) {
        if (Math.random() < 0.15)[r1[i], r2[i]] = [r2[i], r1[i]];
    }

    return [r1, r2];
}

function geneticAlgorithm(population, maxTime, fitnessFn) {
    let i = 0;
    let j = 0;

    let best = population[0];
    let bestFitness = 0;

    const startTime = process.hrtime();
    while (compareTime(maxTime, process.hrtime(startTime))) {
        i++;
        let upJ = false;
        const fitnessArray = [];
        for (let p of population) {
            let f = fitnessFn(p);
            fitnessArray.push(f);

            if (best === null || f > bestFitness) {
                best = p;
                bestFitness = f;
                upJ = true;
            }
        }
        if (upJ) j++;

        // if (j / i < STOP_RATIO) break;

        const newPopulation = [];
        for (let i = 0; i < POPULATION / 2; i++) {
            const a = population[selection(fitnessArray)];
            const b = population[selection(fitnessArray)];

            newPopulation.push(...crossover(a, b).map(mutate));
        }
        population = newPopulation;
    }

    return best;
}

function fitness(word) {
    const lettersUsed = new MapWithDefault(() => ({
        times: 0
    }));
    let points = 0;

    if (!dict.includes(word)) return 0; 

    for (let letter of word) {
        const letterInfo = letters.get(letter);
        const currentInfo = lettersUsed.get(letter);

        if (letterInfo.number === 0) return 0;
        if (++currentInfo.times > letterInfo.number) return 0;
        points += letterInfo.value;
    }
    return points;
}

function main() {
    const result = geneticAlgorithm(initialPopulation, [t, (t * 1e9) % 1e9], fitness);
    console.log(result);
}