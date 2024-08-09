package mohammadali.fouladi.n01547173.mf;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
// Mohammad Ali Fouladi N01547173
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private ArrayList<Course> courseList;
    private DatabaseReference databaseCourses;

    public CourseAdapter(ArrayList<Course> courseList, DatabaseReference databaseCourses) {
        this.courseList = courseList;
        this.databaseCourses = databaseCourses;

    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textViewCourseName.setText(course.getName());
        holder.textViewCourseDescription.setText(course.getDescription());
        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Course currentCourse = courseList.get(pos);
                deleteCourse(currentCourse.getId(), pos);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
    private void deleteCourse(String courseId, int position) {
        if (courseId != null && !courseId.isEmpty()) {
            databaseCourses.child(courseId).removeValue();
            courseList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, courseList.size());
        } else {
        }
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCourseName, textViewCourseDescription;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseName = itemView.findViewById(R.id.textViewCourseName);
            textViewCourseDescription = itemView.findViewById(R.id.textViewCourseDescription);
        }
    }
}
