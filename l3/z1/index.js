const readLine = require('readline');

const STOP_RATIO = 0.5;
const DEVIATION = 0.1;
const TOURNAMENT = 5;
const POPULATION = 20;

Math.gaussian = (mean = 0, deviaiton = 1) => {
    let u = 0,
        v = 0;
    while (u === 0) u = Math.random();
    while (v === 0) v = Math.random();
    return Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2.0 * Math.PI * v) * deviaiton + mean;
}

Math.randInt = (max) => {
    return Math.floor(Math.random() * max);
}

function yang(x, epsilon) {
    return x.reduce((acc, e, i) => acc + epsilon[i] * e ** (i + 1), 0);
}

function mutate(x) {
    return x.map(e => e * Math.gaussian(1, DEVIATION));
}

function crossover(x1, x2) {
    const r1 = [];
    const r2 = x2.map((e, i) => {
        if (Math.random() < 1 / x1.length) {
            r1[i] = e;
            return x1[i];
        } else {
            r1[i] = x1[i];
            return e;
        }
    });

    return [r1, r2];
}

function selection(fitnessArray) {
    let best = Math.randInt(fitnessArray.length);
    for (let i = 0; i < TOURNAMENT; i++) {
        let current = Math.randInt(fitnessArray.length);
        if (fitnessArray[current] < fitnessArray[best]) best = current;
    }
    return best;
}

function compareTime(a, b) {
    return a[0] > b[0] || a[1] > b[1];
}

function geneticAlgorithm(x, maxTime, fitnessFn) {
    let i = 0;
    let j = 0;
    let population = Array(POPULATION).fill(0).map(e => mutate(x));

    let best = null;
    let bestFitness = Infinity;

    const startTime = process.hrtime();
    while (compareTime(maxTime, process.hrtime(startTime))) {
        i++;
        let upJ = false;
        const fitnessArray = [];
        for (let p of population) {
            let f = fitnessFn(p);
            fitnessArray.push(f);

            if (best === null || f < bestFitness) {
                best = p;
                bestFitness = f;
                upJ = true;
            }
        }
        if (upJ) j++;

        if (j / i < STOP_RATIO) break;

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

const r = readLine.createInterface({
    input: process.stdin,
    output: process.stdout
});

r.question('', line => {
    r.close();
    const words = line.split(' ').map(e => Number.parseFloat(e));
    const time = [Math.floor(words[0]), (words[0] * 1e9) % 1e9];
    const x = words.slice(1, 6);
    const e = words.slice(6);

    const res = geneticAlgorithm(x, time, i => yang(i, e));

    console.log(`${res.join(' ')} ${yang(res, e)}`);
});