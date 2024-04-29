import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztesting.LeaderboardEntry
import com.example.quiztesting.R

class LeaderboardAdapter(private val board: MutableList<LeaderboardEntry>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.textName)
        val textScore: TextView = itemView.findViewById(R.id.textScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.leadboard_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = board[position]
        holder.textName.text = entry.name
        holder.textScore.text = entry.score.toString()
    }

    override fun getItemCount(): Int {
        return board.size
    }
}
