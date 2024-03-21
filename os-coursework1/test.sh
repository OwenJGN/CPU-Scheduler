#!/bin/bash

# Set up environment
SIM_COMMAND="java -cp target/classes"
readonly local SCHEDULERS=(RRScheduler SJFScheduler FcfsScheduler FeedbackRRScheduler IdealSJFScheduler)
DIVIDER="==================================================="

# Define input parameters
EXPERIMENT_SEEDS=(
    50 100 200 531 120
    1234 5678 91011 121314 151617
    5124 6789 1234 5678 91011
)

# Input parameters details
EXPERIMENT_PARAMS=(
    "50 0 12 10 10 6"
    "25 0 12 4 24 10"
    "30 0 20 5 35 4"
)

# Simulator parameters
SIM_PARAMS=(
    "10000 1 5 5 0.5 false"
    "5000 10 4 14 0.5 false"
    "5000 10 20 30 0.7 false"
)

# Function to write input parameters
write_input() {
    local file=$1
    local content=$2
    echo -e "$content" > "$file"
    echo "Input parameters written to: $file"
}

# Function to generate input data
generate_input() {
    local params_file=$1
    local output_file=$2
    $SIM_COMMAND InputGen "$params_file" "$output_file" >/dev/null 2>&1
    echo "Generated input data in: $output_file"
}

# Function to write simulator parameters
write_simulator() {
    local file=$1
    local content=$2
    echo -e "$content" > "$file"
    echo "Simulator parameters written to: $file"
}

# Function to run simulation
run_sim() {
    local sim_file=$1
    local input_file=$2
    local output_file=$3
    $SIM_COMMAND Simulator "$sim_file" "$output_file" "$input_file" >/dev/null 2>&1
}

# Function to run an experiment
run_exp() {
    local exp_num=$1
    local folder="Experiments/Exp_$exp_num"
    echo "Running Experiment $exp_num"
    mkdir -p "$folder"

    local input_params=${EXPERIMENT_PARAMS[$exp_num - 1]}
    local sim_params=${SIM_PARAMS[$exp_num - 1]}

    mkdir -p "$folder/input_files"
    mkdir -p "$folder/input_parameters"
    mkdir -p "$folder/simulator_parameters"

    for seed in ${EXPERIMENT_SEEDS[@]}; do
        local input_file="$folder/input_files/input-seed_$seed.in"
        local input_params_file="$folder/input_parameters/input_params-seed_$seed.prp"
        write_input "$input_params_file" "Processes: $input_params Seed: $seed"
        generate_input "$input_params_file" "$input_file"
    done

    for scheduler in ${SCHEDULERS[@]}; do
        echo "----------- Running $scheduler Scheduler -----------"
        mkdir -p "$folder/scheduler_outputs/$scheduler"
        local sim_params_file="$folder/simulator_parameters/${scheduler}_sim_params-seed_$seed.prp"
        write_simulator "$sim_params_file" "Scheduler: $scheduler Time: $sim_params"
        
        for seed in ${EXPERIMENT_SEEDS[@]}; do
            local input_file="$folder/input_files/input-seed_$seed.in"
            local output_file="$folder/scheduler_outputs/$scheduler/output-seed_$seed.out"
            echo "Running simulation with seed: $seed"
            run_sim "$sim_params_file" "$input_file" "$output_file"
        done
    done

    echo $DIVIDER
}

# Main function
main() {
    for ((exp=1; exp<=3; exp++)); do
        run_exp $exp
    done
}

# Execute main function
main
