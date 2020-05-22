const readline = require('readline');
const fs = require('fs');

const STOP_RATIO = 0.1;
const TOURNAMENT = 3;
const POPULATION = 15;
let ELITES = 2;

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
        ELITES = s;
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
    const wordLetters = word.split('');
    const available = {};
    for (let l in letters.dict) {
        available[l] = letters.dict[l].number - wordLetters.filter(e => e === l).length
    }
    const entries = Object.entries(available);
    const availableLetters = entries.filter(e => e[1] > 0).map(e => e[0]);

    if (availableLetters.length === 0) {
        return word;
    } else {
        const sliceIndex = Math.randInt(word.length);
        const letter = shuffleArray(availableLetters).slice(Math.randInt(availableLetters.length)).join('');

        if (Math.random() < 0.5) {
            return word.slice(sliceIndex) + letter + word.slice(0, sliceIndex);
        } else {
            return word.slice(0, sliceIndex) + letter + word.slice(sliceIndex);
        }
    }
}

function crossover(x1, x2) {
    let startSlice = Math.randInt(Math.min(x1.length, x2.length));
    let endSlice = Math.randInt(Math.min(x1.length, x2.length));

    if (endSlice > startSlice)[endSlice, startSlice] = [startSlice, endSlice];

    const r1 = x1.slice(0, startSlice) + x2.slice(startSlice, endSlice) + x1.slice(endSlice);
    const r2 = x2.slice(0, startSlice) + x1.slice(startSlice, endSlice) + x2.slice(endSlice);

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

        let newPopulation = population.map((e, i) => [e, fitnessArray[i]]).sort((a, b) => b[1] - a[1]).map(e => e[0]).slice(0, ELITES);
        newPopulation = newPopulation.filter((e, i) => newPopulation.indexOf(e) === i);
        for (let i = 0; i < (POPULATION - ELITES) / 2; i++) {
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

    for (let letter of word) {
        const letterInfo = letters.get(letter);
        const currentInfo = lettersUsed.get(letter);

        if (letterInfo.number === 0 || ++currentInfo.times > letterInfo.number) {
            points += letterInfo.number - currentInfo.times * letterInfo.value;
        }
        points += letterInfo.value;
    }

    return dict.includes(word) ? points > 0 ? points : -1 / points : points > 0 ? 1 / points : -1 / points;
}

function main() {
    const result = geneticAlgorithm(initialPopulation, [t, (t * 1e9) % 1e9], fitness);
    console.log(fitness(result));
    process.stderr.write(result);
}