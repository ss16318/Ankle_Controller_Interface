function [output] = plotPatientData(patientName)

% Split the patient name into two strings
patientName = split(patientName, ' ');

% Check whether it was possible to convert it into 2 strings (i.e.
% SebastianSteiner not accepted)
try
    patientName = append(patientName(1),'_',patientName(2));
catch
    output = 'Wrong username, retry';
    return
end

% Read from file and save values to a variable
users_file = 'UserList.txt';
usersID = fopen(users_file);
users = textscan(usersID, '%s %s');

% save parameters into an array in the form "Name_Surname"
number_users = length(users{1});
username_list = [""];
for i = 1:number_users
    name = string(users{1}{i});
    surname = string(users{2}{i});
    user_file = append(name,'_',surname);
    username_list(i) = user_file;
end

% Prompt user to give his username
user_input = patientName;
str_user_input = string(user_input);

% set up the flag for ecountring the name
flag = 1;

% Check for username existance, If non existent flag = 0
for i = 1:length(username_list)
    if user_input == username_list(i)
        flag = 0;
    end
end

% send output
if flag == 1
    output = 'Wrong username, retry';
    return
else 
    output = 'Successfull ;-)';        
end

% Open user .txt file
user_file_name = append(str_user_input, '.txt');
users_file = user_file_name;
% Check if it is present, if no file is found display error message
try
    data = readtable(users_file,'Format','%{yyyy-MM-dd}D %f %f %d %d %f %f');
catch
    output = append('No data present for this user!',newline,'Play and save at least one session to display data.');
    return
end

% Get the date
date = table2array(data(:,1));
% Min and Max angles into variables
angles = table2array(data(:,2:3));

% Number of repetitions up and down
reps = table2array(data(:,4:5));

% Score
score = table2array(data(:,6));

% Number of repetitions up and down
time = table2array(data(:,7));

index = [];
j = 1;
% find if someone played on the same day more than once
for i = 1:size(date,1)
    if j > size(date,1)
        break
    else
        indeces = find(date==date(j,1));
        if length(indeces)==1
            new_tab(i,:) = table(date(j,1),angles(j,1),angles(j,2),reps(j,1),reps(j,2),score(j,1),time(j,1));
        else
            min_ang = [];
            max_ang = [];
            reps_up = [];
            reps_down = [];
            scor = [];
            tim = [];
            for k = 1:length(indeces)
                min_ang(k) = angles(indeces(k),1);
                max_ang(k) = angles(indeces(k),2);
                reps_up(k) = reps(indeces(k),1);
                reps_down(k) = reps(indeces(k),2);
                scor(k) = score(indeces(k),1);
                tim(k) = time(indeces(k),1);
            end
            min_ang = get_average(min_ang);
            max_ang = get_average(max_ang);
            scor = get_average(scor);
            reps_up = sum(reps_up);
            reps_down = sum(reps_down);
            tim = sum(tim);
            new_tab(i,:) = table(date(j,1),min_ang,max_ang,reps_up,reps_down,scor,tim);
        end
        j = j+length(indeces);
    end
end

% Get the date
date = table2array(new_tab(:,1));
% Min and Max angles into variables
angles = table2array(new_tab(:,2:3));

% Number of repetitions up and down
reps = table2array(new_tab(:,4:5));

% Score
score = table2array(new_tab(:,6));

% Number of repetitions up and down
time = table2array(new_tab(:,7));

% Find the total time
tot_time = zeros(1,length(time));
for i = 1:length(time)
    if i == 1
        
    else
        tot_time(i) = tot_time(i-1) + time(i-1);
    end
end
tot_time = tot_time';
final_time = [tot_time time];

% RED  ==> MIN
% CYAN ==> MAX

% Plot the different angles against date
figure(1)
subplot(2,2,1);
plot(date, angles(:,2),'c');
hold on
plot(date, angles(:,1),'r');
scatter(date, angles(:,1),'r', 'filled');
scatter(date, angles(:,2),'c', 'filled');
hold off
ylim([-90,90]);
xlabel('Date','FontSize',12,'FontWeight','bold','Color','b');
ylabel('Degrees','FontSize',12,'FontWeight','bold','Color','b');
title('Angles over Days','FontSize',16,'FontWeight','bold','Color','r');
legend('Max Angle','Min Angle','FontSize',12,'FontWeight','bold','Color','y')

subplot(2,2,2);
b = bar(date, reps,'stacked');
b(1).FaceColor = 'c';
b(2).FaceColor = 'r';
xlabel('Date','FontSize',12,'FontWeight','bold','Color','b');
ylabel('Repetitions','FontSize',12,'FontWeight','bold','Color','b');
title('Repetitions over Days','FontSize',16,'FontWeight','bold','Color','r');
legend('UP','DOWN','FontSize',12,'FontWeight','bold','Color','y')

subplot(2,2,3);
b = bar(date, final_time,'stacked');
b(1).FaceColor = '#808080';
b(2).FaceColor = 'b';
xlabel('Date','FontSize',12,'FontWeight','bold','Color','b');
ylabel('Total Time Played','FontSize',12,'FontWeight','bold','Color','b');
title('Total time Played over Days','FontSize',16,'FontWeight','bold','Color','r');
legend('Previous Sessions','Current Session','FontSize',12,'FontWeight','bold','Color','y')

subplot(2,2,4);
bar(date, score,'g');
ylim([0,100]);
xlabel('Date','FontSize',12,'FontWeight','bold','Color','b');
ylabel('% Smoothness Score','FontSize',12,'FontWeight','bold','Color','b');
title('% Smoothness Score over Days','FontSize',16,'FontWeight','bold','Color','r');

str_user_input = strrep(str_user_input,'_',' ');
sgtitle(str_user_input, 'FontSize',22,'FontWeight','bold','Color','k')
end

function [average] = get_average(data)
    sum = 0;
    for i=1:length(data)
        sum = sum + data(i);
    end
    average = sum/length(data);
end