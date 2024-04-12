RUN="java -cp target/classes"
SCHEDULERS=(
	RRScheduler 
	SJFScheduler 
	FcfsScheduler 
	FeedbackRRScheduler 
	IdealSJFScheduler
)

EXPERIMENT_SEEDS=(
    "12382 68846 82050 5112 97108"
    "11654 93333 24870 8621 10291"
    "80253 41649 72592 7885 36334"
)

EXPERIMENT_PARAMS=(
    "500 0 10 30 20 5"
    "100 0 12 40 20 5"
    "50 0 12 10 10 12"
)

EXPERIMENT_SIM_PARAMS=(
    "40000 1 12 30 0.5 false"
    "10000 5 15 40 0.5 false"
    "10000 1 5 10 0.5 false"
)

write_input_parameters() {
    local file_name=$1
    printf "numberOfProcesses=%s\nstaticPriority=%s\nmeanInterArrival=%s\nmeanCpuBurst=%s\nmeanIoBurst=%s\nmeanNumberBursts=%s\nseed=%s\n" \
        $2 $3 $4 $5 $6 $7 $8 > "$file_name"
		
    echo -e "\nInput parameters written in $file_name"
}
write_simulator_parameters() {
    local simulator_parameters_file=$1
	printf "scheduler=%s\ntimeLimit=%s\ninterruptTime=%s\ntimeQuantum=%s\ninitialBurstEstimate=%s\nalphaBurstEstimate=%s\nperiodic=%s\n" \
        $2 $3 $4 $5 $6 $7 $8 > "$simulator_parameters_file"

    echo "Simulator data written in $simulator_parameters_file"
}
run_inputgenerator() {
	$RUN InputGenerator $1 $2 >/dev/null 2>&1

	echo "Input data written in $2"
}
run_simulator() {
	$RUN Simulator $1 $3 $2 >/dev/null 2>&1
	
	echo "Output data written in $3"
}

run_experiment() {
    experiment_number=$1
    folder="Experiments/$experiment_number"
    echo -e "\nRunning Experiment $experiment_number"

    mkdir -p "$folder/input_parameters" "$folder/input_files"

    input_parameters="${EXPERIMENT_PARAMS[$((experiment_number - 1))]}"
    sim_parameters="${EXPERIMENT_SIM_PARAMS[$((experiment_number - 1))]}"
    seeds="${EXPERIMENT_SEEDS[$((experiment_number - 1))]}"
    
    for seed in ${seeds[@]}; do
		data_file="$folder/input_files/input-seed_$seed.in"
		input_params_file="$folder/input_parameters/input_parameters-seed_$seed.prp"
    
		write_input_parameters "$input_params_file" "${input_parameters[@]}" "$seed"
		run_inputgenerator "$input_params_file" "$data_file"
    
		for scheduler in "${SCHEDULERS[@]}"; do
			mkdir -p "$folder/scheduler_outputs/$scheduler" "$folder/simulator_parameters"
			sim_params_file="$folder/simulator_parameters/${scheduler}_simulator_parameters.prp"
        
			write_simulator_parameters "$sim_params_file" "$scheduler" "${sim_parameters[@]}"
        
			input_data_file="$folder/input_files/input-seed_$seed.in"
			run_simulator "$sim_params_file" "$input_data_file" "$folder/scheduler_outputs/$scheduler/output-seed_$seed.out"
		done
	done
}

main() {
    for experiment_number in {1..3}; do
        run_experiment "$experiment_number"
    done
}

main