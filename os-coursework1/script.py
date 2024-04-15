def calculate_metrics(file_path):
    total_waiting_time = 0
    total_turnaround_time = 0
    total_processes = 0
    terminated_time_id_0 = 0

    with open(file_path, 'r') as file:
        next(file)
        for line in file:
            columns = line.strip().split('\t')
            process_id = int(columns[0])
            creation_time = int(columns[2])
            terminated_time = int(columns[4])
            waiting_time = int(columns[9])

            if process_id == 0:
                terminated_time_id_0 = terminated_time

            total_waiting_time += waiting_time
            turnaround_time = terminated_time - creation_time
            total_turnaround_time += turnaround_time
            total_processes += 1

    overall_throughput = total_processes / terminated_time_id_0
    avg_turnaround_time = total_turnaround_time / total_processes
    avg_waiting_time = total_waiting_time / total_processes
    return avg_waiting_time, overall_throughput, avg_turnaround_time


def run_experiment(seeds, experiment_num):
    scheduler_data = {sch: {"average_waiting_time": 0, "throughput": 0, "average_turnaround_time": 0} for sch in
                      schedulers}

    with open(f"Experiments\\{experiment_num}\\Experiment_{experiment_num}.txt", "w") as f:
        for seed in seeds:
            f.write("-----------------------------------------------------------\n")
            f.write(f"Current seed: {seed}\n")
            for sch in schedulers:
                file_path = f"Experiments\\" \
                            f"{experiment_num}\\scheduler_outputs\\{sch}\\output-seed_{seed}.out"
                wt, tp, tat = calculate_metrics(file_path)
                f.write(f"Scheduler: {sch}\n")
                if experiment_num == 1:
                    f.write(f"Average Waiting Time: {wt}\n")
                    f.write(f"Average throughput: {tp}\n\n")
                elif experiment_num == 2:
                    f.write(f"Average Waiting Time: {wt}\n\n")
                elif experiment_num == 3:
                    f.write(f"Average Turnaround Time: {tat}\n\n")

                scheduler_data[sch]["average_waiting_time"] += wt
                scheduler_data[sch]["throughput"] += tp
                scheduler_data[sch]["average_turnaround_time"] += tat

        f.write("-----------------------------------------------------------\n")
        f.write("\nOverall averages:\n")
        for sch, data in scheduler_data.items():
            f.write(f"Scheduler: {sch}\n")
            if experiment_num != 3:
                f.write(f"Average Waiting Time: {data['average_waiting_time'] / 5}\n")
            if experiment_num == 1:
                f.write(f"Average throughput: {data['throughput'] / 5}\n")
            elif experiment_num == 3:
                f.write(f"Average Turnaround Time: {data['average_turnaround_time'] / 5}\n")
            f.write("\n")

        print(f"Successfully written Experiment {experiment_num} metrics to Experiment_{experiment_num}.txt")


schedulers = ["FcfsScheduler", "RRScheduler", "SJFScheduler", "IdealSJFScheduler", "FeedbackRRScheduler"]
experiment_seeds = [["12382", "68846", "82050", "5112", "97108"],
                    ["11654", "93333", "24870", "8621", "10291"],
                    ["80253", "41649", "72592", "7885", "36334"]]

for i, exp_seeds in enumerate(experiment_seeds):
    run_experiment(exp_seeds, i + 1)
